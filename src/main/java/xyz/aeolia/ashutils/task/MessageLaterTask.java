package xyz.aeolia.ashutils.task;

import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.aeolia.ashutils.sender.MessageSender;

public class MessageLaterTask extends BukkitRunnable {
  CommandSender sender;
  String message;

  public MessageLaterTask(CommandSender sender, String message) {
    this.sender = sender;
    this.message = message;
  }

  @Override
  public void run() {
    MessageSender.sendMessage(sender, message);
  }
}
