package xyz.aeolia.ashutils.command

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import xyz.aeolia.ashutils.sender.MessageSender

class NotEnabledCommandExecutor : CommandExecutor {
  override fun onCommand(
    sender: CommandSender,
    command: Command,
    label: String,
    args: Array<out String>
  ): Boolean {
    MessageSender.sendMessage(sender, "This command is not enabled on this server due to a missing" +
            " softdepend.")
    return true
  }
}