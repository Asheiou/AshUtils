package xyz.aeolia.lib.listener

import hk.siggi.bukkit.plugcubebuildersin.world.WorldBlock
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.plugin.java.JavaPlugin
import xyz.aeolia.lib.manager.UserManager
import xyz.aeolia.lib.sender.MessageSender
import xyz.aeolia.lib.serializable.User

class PVPListener(val plugin: JavaPlugin) : Listener {
  val playersWarned = mutableListOf<Player>()
  val globalBlocks = mutableMapOf<(Location), WorldBlock>()

  @EventHandler(priority = EventPriority.LOWEST)
  fun onBlockPlace (event: BlockPlaceEvent) {
    val player = event.player
    val user = processBlockEvent(player, event.isCancelled, plugin) ?: return
    if (!user.modMode) {
      user.pvpBlocks.add(WorldBlock(event.block))
      globalBlocks.put(event.block.location, WorldBlock(event.block))
      if (player !in playersWarned) {
        MessageSender.sendMessage(player, "Blocks placed here will be deleted when you die or leave the world!")
        playersWarned.add(player)
      }
    } else {
      playersWarned.remove(player)
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  fun onBlockBreak(event: BlockBreakEvent) {
    val location = event.block.location
    val worldBlock = globalBlocks[location]?: return
    val user = worldBlock.placer?: return
    user.pvpBlocks.remove(worldBlock)
  }

  @EventHandler(priority = EventPriority.LOWEST)
  fun onPlayerWorldChange(event: PlayerChangedWorldEvent) {
    val player = event.player
    val from = event.from
    if (from.name !in plugin.config.getStringList("pvp.worlds")) return
    clearBlocks(UserManager.getUser(player))
  }

  @EventHandler(priority = EventPriority.LOWEST)
  fun onPlayerDeath(event: PlayerDeathEvent) {
    val player = event.player
    val world = player.world
    if(world.name !in plugin.config.getStringList("pvp.worlds")) return
    clearBlocks(UserManager.getUser(player))
  }

  companion object {
    fun clearBlocks(user: User) {
      user.pvpBlocks.forEach { block ->
        block.bukkitBlock.type = Material.AIR
      }
      user.pvpBlocks.clear()
    }

    fun processBlockEvent(player: Player, cancelled: Boolean, plugin: JavaPlugin): User? {
      val world = player.location.world
      if (world.name !in plugin.config.getStringList("pvp.worlds")) {
        return null
      }
      if (cancelled) return null
      return UserManager.getUser(player)
    }
  }
}