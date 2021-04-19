package world.cepi.example

import net.minestom.server.entity.Player

internal interface Handler {
    fun register(playerInit: Player)
}