package uk.asheiou.ashutils.command.ashutils;

import org.bukkit.command.CommandSender;

import uk.asheiou.ashutils.MessageSender;
import uk.asheiou.ashutils.restartonempty.StatusManager;

public class RestartOnEmptyHandler {
  public static boolean doUtilsROE(CommandSender sender, String[] args) {
    if (args.length > 0) {
      if (args.length > 1) {
        MessageSender.sendMessage(sender, "Too many arguments! Usage:");
        return false;
      }
      switch (args[0].toLowerCase()) {
      case "true":
        StatusManager.setStatus(true);
        MessageSender.sendMessage(sender, "The server will restart on empty.");
        return true;
      case "false":
        StatusManager.setStatus(false);
        MessageSender.sendMessage(sender, "The server will no longer restart on empty.");
        return true;
      case "status":
        MessageSender.sendMessage(sender,
            "The server " + (StatusManager.getStatus() ? "will" : "will not") + " restart on empty.");
        return true;
      default:
        MessageSender.sendMessage(sender, "Argument not recognised! Usage:");
        return false;
      }
    }
    boolean toSet = !StatusManager.getStatus();
    StatusManager.setStatus(toSet);
    MessageSender.sendMessage(sender,
        "The server " + (toSet ? "will" : "will no longer") + " restart on empty.");
    return true;
  }
}

