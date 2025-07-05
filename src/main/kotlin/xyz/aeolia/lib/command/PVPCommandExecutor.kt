package xyz.aeolia.lib.command

import io.papermc.paper.command.brigadier.argument.ArgumentTypes.player
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import xyz.aeolia.lib.listener.PVPListener
import xyz.aeolia.lib.menu.PVPMenu
import xyz.aeolia.lib.sender.MessageSender
import xyz.aeolia.lib.utils.Message

class PVPCommandExecutor(plugin1: JavaPlugin) : CommandExecutor, PVPMenu(plugin1) {
  override fun onCommand(
    sender: CommandSender,
    command: Command,
    label: String,
    args: Array<out String>
  ): Boolean {
    if (sender !is Player) {
      MessageSender.sendMessage(sender, Message.Generic.NOT_PLAYER)
      return true
    }
    if (args.isEmpty()||(!sender.hasPermission("lib.pvp.manage"))) {
      inventory.open(sender)
      return true
    }

    if(args[0] == "respawn") {
      if (args.size == 1) {
        PVPListener.tpPlayerToArena(sender, plugin)
      } else {
        var condition = false
        Bukkit.getOnlinePlayers().forEach { player ->
          if (player.name == args[1]) {
            PVPListener.tpPlayerToArena(player, plugin)
            condition = true
          }
        }
        if (!condition) {
          MessageSender.sendMessage(sender, Message.Player.NOT_FOUND)
        }
        return true
      }
    }
    return false
  }
}