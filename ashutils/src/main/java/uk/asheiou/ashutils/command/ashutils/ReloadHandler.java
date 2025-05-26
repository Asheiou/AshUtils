package uk.asheiou.ashutils.command.ashutils;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import uk.asheiou.configmanager.ConfigManager;

import uk.asheiou.ashutils.MessageSender;

public class ReloadHandler {
  public static boolean doUtilReload(JavaPlugin plugin, CommandSender sender) {
    MessageSender.sendMessage(sender, "Starting config reload...");
    int response = new ConfigManager(plugin).loadConfig();
    String compose = "Reload complete! ";
    switch(response) {
    case -1:
      compose += "Your config file was empty, deleted, or unreadable. It has been replaced with the default.";
      break;
    case 0:
      break;
    case 1:
      compose += "Added " + response + " missing value.";
    default:
      compose += "Added " + response + " missing values.";
      break;
    }
    MessageSender.sendMessage(sender, compose);
    return true;
  }
}
