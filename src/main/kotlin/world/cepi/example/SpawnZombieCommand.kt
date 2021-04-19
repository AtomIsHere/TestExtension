package world.cepi.example

import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Player

class SpawnZombieCommand(name: String = "zombie") : Command(name) {

    init {
        setDefaultExecutor { sender, _ ->
            if(sender is Player) {
                val zombie = ZombieCreature()
                zombie.setInstance(sender.instance!!, sender.position.clone())
            }
        }
    }
}