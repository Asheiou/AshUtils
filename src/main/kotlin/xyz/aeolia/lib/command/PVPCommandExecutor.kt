package xyz.aeolia.lib.command

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import xyz.aeolia.lib.menu.PVPMenu

class PVPCommandExecutor(plugin1: JavaPlugin) : CommandExecutor, PVPMenu(plugin1) {
  override fun onCommand(
    sender: CommandSender,
    command: Command,
    label: String,
    args: Array<out String>
  ): Boolean {
    if (sender !is Player) return true
    INVENTORY.open(sender)
    return true
  }
}