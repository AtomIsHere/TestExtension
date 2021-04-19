package world.cepi.example

import net.minestom.server.MinecraftServer
import net.minestom.server.entity.LivingEntity
import net.minestom.server.entity.Player
import net.minestom.server.entity.damage.DamageType
import net.minestom.server.entity.damage.EntityDamage
import net.minestom.server.event.entity.EntityDamageEvent
import net.minestom.server.event.player.PlayerUseItemEvent
import net.minestom.server.utils.Vector
import net.minestom.server.utils.time.TimeUnit

object ParryHandler : Handler {
    val isParrying = mutableListOf<Player>()

    override fun register(playerInit: Player) {
        val scheduleManager = MinecraftServer.getSchedulerManager()
        playerInit.addEventCallback(PlayerUseItemEvent::class.java) {
            with(it) {
                if(hand == Player.Hand.OFF && !isParrying.contains(player)) {
                    isParrying.add(player)
                    scheduleManager.buildTask {
                        if(isParrying.contains(player)) {
                            isParrying.remove(player)
                        }
                    }.delay(1, TimeUnit.SECOND).schedule()
                }
            }
        }

        playerInit.addEventCallback(EntityDamageEvent::class.java) {
            with(it) {
                if(damageType is EntityDamage && (damageType as EntityDamage).source is LivingEntity && entity is Player) {
                    val player = entity as Player
                    val source = (damageType as EntityDamage).source as LivingEntity

                    if(isParrying.contains(player)) {
                        isCancelled = true
                        source.damage(DamageType.VOID, damage*0.75F) //TODO: Create damage type for parrying
                        source.velocity = Vector(0.0, 5.0, 0.0)
                        isParrying.remove(player)
                    }
                }
            }
        }
    }
}