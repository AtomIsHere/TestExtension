package world.cepi.example.zombie

import net.minestom.server.entity.Entity
import net.minestom.server.utils.time.TimeUnit

interface Parryable {
    fun parryableAttack(target: Entity, range: Double, delay: Long, timeUnit: TimeUnit)
    fun isAttacking(): Boolean
}