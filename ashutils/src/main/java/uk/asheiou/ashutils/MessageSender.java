package uk.asheiou.ashutils;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class MessageSender {
  public static void sendMessage(CommandSender recipient, String type, String message) {
    String prefix = ChatColor.translateAlternateColorCodes('&', JavaPlugin.getProvidingPlugin(AshUtils.class).getConfig().getString("chat-prefix"));
    ChatColor colourCode; 
    switch(type) {
    case "err":
      colourCode = ChatColor.RED;
    case "info":
      colourCode = ChatColor.AQUA;
    case "ok":
      colourCode = ChatColor.GREEN;
    default:
      colourCode = ChatColor.WHITE;
    }
    recipient.sendMessage(prefix + ChatColor.RESET + colourCode + " " + message);
  }
}
