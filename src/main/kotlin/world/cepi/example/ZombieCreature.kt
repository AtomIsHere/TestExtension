package world.cepi.example

import net.minestom.server.entity.*
import net.minestom.server.entity.ai.EntityAIGroupBuilder
import net.minestom.server.entity.ai.goal.FollowTargetGoal
import net.minestom.server.entity.ai.goal.MeleeAttackGoal
import net.minestom.server.entity.ai.goal.RandomLookAroundGoal
import net.minestom.server.entity.ai.target.ClosestEntityTarget
import net.minestom.server.entity.damage.EntityDamage
import net.minestom.server.utils.time.TimeUnit
import net.minestom.server.utils.time.UpdateOption

class ZombieCreature : EntityCreature(EntityType.ZOMBIE) {
    init {
        addAIGroup(EntityAIGroupBuilder()
            .addGoalSelector(RandomLookAroundGoal(this, 20))
            .addGoalSelector(FollowTargetGoal(this, UpdateOption(1, TimeUnit.SECOND)))
            .addGoalSelector(MeleeAttackGoal(this, 1.0, 1, TimeUnit.SECOND))
            .addTargetSelector(ClosestEntityTarget(this, 10.0F, Player::class.java))
            .build())
    }

    override fun attack(target: Entity, swingHand: Boolean) {
        super.attack(target, swingHand)
        if(target is LivingEntity) {
            target.damage(EntityDamage(this), 2.0F)
        }
    }
}