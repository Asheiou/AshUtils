package cymru.asheiou.ashutils.command.ashutils;

import org.bukkit.command.CommandSender;

import cymru.asheiou.ashutils.manager.StatusManager;
import cymru.asheiou.ashutils.sender.MessageSender;

public class StatusToggleHandler {
  public static boolean doToggleStatus(CommandSender sender, String[] args, String instance) {
    String instanceFormatted = switch (instance) {
      case "restartonempty" -> "RestartOnEmpty";
      case "lockchat" -> "Chat lock";
      default -> "Unknown instance";
    };if (args.length > 0) {
      if (args.length > 1) {
        MessageSender.sendMessage(sender, "Too many arguments! Usage: /ashutils "+instance+ " [true/false/status].");
      }
      switch (args[0].toLowerCase()) {
      case "true":
        StatusManager.setStatus(instance, true);
        MessageSender.sendMessage(sender, instanceFormatted + " enabled.");
        return true;
      case "false":
        StatusManager.setStatus(instance, false);
        MessageSender.sendMessage(sender, instanceFormatted + " disabled.");
        return true;
      case "status":
        MessageSender.sendMessage(sender,
            instanceFormatted + " " + (StatusManager.getStatus(instance) ? "enabled" : "disabled") + ".");
        return true;
      default:
        MessageSender.sendMessage(sender, "Argument not recognised! Usage:");
        return false;
      }
    }
    boolean toSet = !StatusManager.getStatus(instance);
    StatusManager.setStatus(instance, toSet);
    MessageSender.sendMessage(sender, instanceFormatted + " is " + (toSet ? "enabled" : "disabled") + ".");
    return true;
  }
}

