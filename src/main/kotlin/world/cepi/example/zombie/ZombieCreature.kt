package world.cepi.example.zombie

import net.kyori.adventure.sound.Sound
import net.minestom.server.MinecraftServer
import net.minestom.server.entity.*
import net.minestom.server.entity.ai.EntityAIGroupBuilder
import net.minestom.server.entity.ai.goal.FollowTargetGoal
import net.minestom.server.entity.ai.goal.RandomLookAroundGoal
import net.minestom.server.entity.ai.target.ClosestEntityTarget
import net.minestom.server.entity.damage.EntityDamage
import net.minestom.server.sound.SoundEvent
import net.minestom.server.utils.time.TimeUnit
import net.minestom.server.utils.time.UpdateOption

class ZombieCreature : EntityCreature(EntityType.ZOMBIE), Parryable {
    private var attacking = false

    init {
        addAIGroup(EntityAIGroupBuilder()
            .addGoalSelector(RandomLookAroundGoal(this, 20))
            .addGoalSelector(FollowTargetGoal(this, UpdateOption(1, TimeUnit.SECOND)))
            .addGoalSelector(ParryableAttackGoal(this, 1.0, 20, 10, TimeUnit.TICK))
            .addTargetSelector(ClosestEntityTarget(this, 10.0F, Player::class.java))
            .build())
    }

    override fun attack(target: Entity, swingHand: Boolean) {
        super.attack(target, swingHand)
        if(target is LivingEntity) {
            target.damage(EntityDamage(this), 2.0F)
        }
    }

    override fun parryableAttack(target: Entity, range: Double, delay: Long, timeUnit: TimeUnit) {
        if(!attacking) {
            attacking = true

            if(target is Player) {
                target.playSound(Sound.sound(SoundEvent.BLOCK_NOTE_BLOCK_PLING, Sound.Source.HOSTILE, 1.0F, 2.0F))
            }

            MinecraftServer.getSchedulerManager().buildTask {
                if(getDistance(target) <= range) {
                    attack(target, true)
                }
                attacking = false
            }.delay(delay, timeUnit).schedule()
        }
    }

    override fun isAttacking(): Boolean {
        return attacking
    }
}