package xyz.aeolia.ashutils.sender;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.aeolia.ashutils.AshUtils;

public class MessageSender {
  public static void sendMessage(CommandSender recipient, String message) {
    String prefix = JavaPlugin.getProvidingPlugin(AshUtils.class).getConfig().getString("chat-prefix")
            .replace('&', ChatColor.COLOR_CHAR);
    recipient.sendMessage(prefix + ChatColor.RESET + " " + message);
  }
}
