package xyz.aeolia.ashutils.command.admin.ashutils;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;
import xyz.aeolia.ashutils.sender.MessageSender;

public class MotdHandler {
  public static String motd;

  public static String getMotd() {
    return motd;
  }

  public static void setMotd(@Nullable String motdToSet) {
    motd = motdToSet;
  }

  public static boolean handleCommand(CommandSender sender, String[] args) {
    if (args.length == 0) {
      setMotd(null);
      MessageSender.sendMessage(sender, "MOTD reset!");
    } else {
      setMotd(String.join(" ", args));
      MessageSender.sendMessage(sender, "MOTD set to: ");
      MessageSender.sendMessage(sender, getMotd());
      MessageSender.sendMessage(sender, "It will persist until the server restarts, unless you reset it by " +
              "running this command again without an argument.");
    }
    return true;
  }
}
