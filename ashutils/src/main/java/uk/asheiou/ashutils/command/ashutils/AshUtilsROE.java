package uk.asheiou.ashutils.command.ashutils;

import org.bukkit.command.CommandSender;

import uk.asheiou.ashutils.MessageSender;
import uk.asheiou.ashutils.restartonempty.ROEToggle;

public class AshUtilsROE {
  public static boolean doUtilsROE(CommandSender sender, String[] args) {
    if (args.length > 0) {
      if (args.length > 1) {
        MessageSender.sendMessage(sender, "Too many arguments! Usage:");
        return false;
      }
      switch (args[0].toLowerCase()) {
      case "true":
        ROEToggle.setStatus(true);
        MessageSender.sendMessage(sender, "The server will restart on empty.");
        return true;
      case "false":
        ROEToggle.setStatus(false);
        MessageSender.sendMessage(sender, "The server will no longer restart on empty.");
        return true;
      case "status":
        MessageSender.sendMessage(sender,
            "The server " + (ROEToggle.getStatus() ? "will" : "will not") + " restart on empty.");
        return true;
      default:
        MessageSender.sendMessage(sender, "Argument not recognised! Usage:");
        return false;
      }
    }
    boolean toSet = !ROEToggle.getStatus();
    ROEToggle.setStatus(toSet);
    MessageSender.sendMessage(sender,
        "The server " + (toSet ? "will" : "will no longer") + " restart on empty.");
    return true;
  }
}

