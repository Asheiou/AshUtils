package xyz.aeolia.ashutils.command.admin.ashutils;

import cymru.asheiou.configmanager.ConfigManager;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.aeolia.ashutils.sender.MessageSender;

public class ReloadHandler {
  public static boolean doUtilReload(CommandSender sender, JavaPlugin plugin) {
    MessageSender.sendMessage(sender, "Starting config reload...");
    Integer[] response = new ConfigManager(plugin, true).loadConfig();
    String compose = "Reload complete! ";
    if (response[0] == -1) {
      compose += "Your config file was empty, deleted, or unreadable. It has been replaced with the default.";
    } else if (response[0] > 0 || response[1] > 0) {
      compose += "Added " + response[0] + " value" + (response[0] == 1 ? "" : "s") + ", removed "
              + response[1] + " value" + (response[1] == 1 ? "" : "s") + ".";
    }
    MessageSender.sendMessage(sender, compose);
    return true;
  }
}
