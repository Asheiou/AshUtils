package xyz.aeolia.ashutils.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xyz.aeolia.ashutils.sender.MessageSender;

public class NotEnabledCommandExecutor implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    MessageSender.sendMessage(sender, "This command is not enabled on this server due to a missing softdepend.");
    return true;
  }
}
