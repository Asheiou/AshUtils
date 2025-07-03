package xyz.aeolia.ashutils.command.admin.ashutils

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import xyz.aeolia.ashutils.sender.MessageSender

class ClearChatHandler {
  companion object {
    @JvmStatic
    fun doClearChat() : Boolean {
      for (player in Bukkit.getOnlinePlayers()) {
        if(!player.hasPermission("ashutils.clearchat.exempt")) {
          repeat (100) {
            player.sendMessage(Component.text(" "))
          }
        } else {
          MessageSender.sendMessage(player, "Chat cleared for non-exempt users.")
        }
      }
      return true
    }
  }
}