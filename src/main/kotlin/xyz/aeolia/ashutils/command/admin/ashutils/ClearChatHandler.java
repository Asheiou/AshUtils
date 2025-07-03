package xyz.aeolia.ashutils.command.admin.ashutils;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.aeolia.ashutils.sender.MessageSender;

public class ClearChatHandler {
  public static boolean doClearChat(JavaPlugin plugin) {
    for (Player player : plugin.getServer().getOnlinePlayers()) {
      if (!player.hasPermission("ashutils.clearchat.exempt")) {
        for (int i = 0; i < 100; i++) {
          player.sendMessage(" ");
        }
      } else {
        MessageSender.sendMessage(player, "Chat cleared for non-exempt users.", true);
      }
    }
    return true;
  }
}
