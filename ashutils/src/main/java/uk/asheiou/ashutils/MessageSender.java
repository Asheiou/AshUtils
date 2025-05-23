package uk.asheiou.ashutils;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class MessageSender {
  public static void sendMessage(CommandSender recipient, String message) {
    String prefix = ChatColor.translateAlternateColorCodes('&', JavaPlugin.getProvidingPlugin(AshUtils.class).getConfig().getString("chat-prefix"));
    recipient.sendMessage(prefix + ChatColor.RESET + " " + message);
  }
}
