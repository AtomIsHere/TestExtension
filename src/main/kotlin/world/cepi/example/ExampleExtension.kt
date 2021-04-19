package world.cepi.example

import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.minestom.server.extensions.Extension;

class ExampleExtension : Extension() {

    private val playerInitialization: (Player) -> Unit = { player ->
        ParryHandler.register(player)
    }

    override fun initialize() {
        MinecraftServer.getCommandManager().register(SpawnZombieCommand())

        val connectionManager = MinecraftServer.getConnectionManager()
        connectionManager.addPlayerInitialization(playerInitialization)

        logger.info("[ExampleExtension] has been enabled!")
    }

    override fun terminate() {
        val connectionManager = MinecraftServer.getConnectionManager()
        connectionManager.removePlayerInitialization(playerInitialization)

        logger.info("[ExampleExtension] has been disabled!")
    }

}