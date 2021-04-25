package world.cepi.example.zombie

import net.minestom.server.entity.Entity
import net.minestom.server.entity.EntityCreature
import net.minestom.server.entity.ai.GoalSelector
import net.minestom.server.utils.time.Cooldown
import net.minestom.server.utils.time.TimeUnit
import net.minestom.server.utils.time.UpdateOption

class ParryableAttackGoal(
    entityCreature: EntityCreature,
    private val range: Double,
    private val delay: Long,
    private val parryDelay: Long,
    private val timeUnit: TimeUnit,
) : GoalSelector(entityCreature) {
    private val cooldown = Cooldown(UpdateOption(5, TimeUnit.TICK))

    private var lastHit: Long = 0

    private var cachedTarget: Entity? = null
    private var stop: Boolean = false

    override fun shouldStart(): Boolean {
        cachedTarget = findTarget()
        return cachedTarget != null
    }

    override fun start() {
        val targetPosition = cachedTarget!!.position
        entityCreature.navigator.setPathTo(targetPosition)
    }

    override fun tick(time: Long) {
        val target: Entity?
        if (cachedTarget != null) {
            target = cachedTarget
            cachedTarget = null
        } else {
            target = findTarget()
        }

        this.stop = target == null

        if (!stop) {
            // Attack the entity
            if (entityCreature.getDistance(target!!) <= range) {
                if (!Cooldown.hasCooldown(time, lastHit, timeUnit, delay + parryDelay) && entityCreature is Parryable && !(entityCreature as Parryable).isAttacking()) {
                    (entityCreature as Parryable).parryableAttack(target, range, delay, timeUnit)
                    this.lastHit = time
                }
                return
            }

            val navigator = entityCreature.navigator
            val pathPosition = navigator.pathPosition
            val targetPosition = target.position
            if (pathPosition == null || !pathPosition.isSimilar(targetPosition)) {
                if (cooldown.isReady(time)) {
                    cooldown.refreshLastUpdate(time)
                    navigator.setPathTo(targetPosition)
                }
            }
        }
    }

    override fun shouldEnd(): Boolean {
        return stop
    }

    override fun end() {
        entityCreature.navigator.setPathTo(null)
    }
}