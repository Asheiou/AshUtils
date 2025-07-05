package xyz.aeolia.ashutils.command.admin.ashutils;

import org.bukkit.command.CommandSender;
import xyz.aeolia.ashutils.manager.StatusManager;
import xyz.aeolia.ashutils.sender.MessageSender;

import static xyz.aeolia.ashutils.utils.Message.Generic.COMMAND_USAGE;
import static xyz.aeolia.ashutils.utils.Message.Generic.TOO_MANY_ARGS;

public class StatusToggleHandler {
  public static boolean doToggleStatus(CommandSender sender, String[] args, String instance) {
    instance = switch (instance) {
      case "roe" -> "restartonempty";
      case "lc" -> "lockchat";
      default -> instance;
    };
    String instanceFormatted = switch (instance) {
      case "restartonempty" -> "RestartOnEmpty";
      case "lockchat" -> "Chat lock";
      default -> "Unknown instance";
    };
    if (args.length > 0) {
      if (args.length > 1) {
        MessageSender.sendMessage(sender, TOO_MANY_ARGS, true);
        MessageSender.sendMessage(sender, "/ashutils " + instance + " [true/false/status].", true);
      }
      switch (args[0].toLowerCase()) {
        case "true":
          StatusManager.setStatus(instance, true);
          MessageSender.sendMessage(sender, instanceFormatted + " enabled.", true);
          return true;
        case "false":
          StatusManager.setStatus(instance, false);
          MessageSender.sendMessage(sender, instanceFormatted + " disabled.", true);
          return true;
        case "status":
          MessageSender.sendMessage(sender,
                  instanceFormatted + " " + (StatusManager.getStatus(instance) ? "enabled" : "disabled") + ".",
                  true);
          return true;
        default:
          MessageSender.sendMessage(sender, COMMAND_USAGE, true);
          return false;
      }
    }
    boolean toSet = !StatusManager.getStatus(instance);
    StatusManager.setStatus(instance, toSet);
    MessageSender.sendMessage(sender, instanceFormatted + " is " + (toSet ? "enabled" : "disabled") + ".", true);
    return true;
  }
}

