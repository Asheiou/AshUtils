package xyz.aeolia.ashutils.command.admin

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import xyz.aeolia.ashutils.instance.Message.Error.REQUEST_FAIL_GENERIC
import xyz.aeolia.ashutils.instance.Message.Generic.COMMAND_USAGE
import xyz.aeolia.ashutils.sender.MessageSender
import xyz.aeolia.ashutils.sender.WebhookSender

class BroadcastCommandExecutor(val plugin: JavaPlugin) : CommandExecutor {
  override fun onCommand(
    sender: CommandSender,
    command: Command,
    label: String,
    args: Array<out String>): Boolean {
    if (args.isEmpty()) {
      MessageSender.sendMessage(sender, COMMAND_USAGE, true)
    }
    val message = MessageSender.miniMessage.deserialize(args.joinToString(" "))
    val broadcastStatus = WebhookSender.broadcast(message)
    if (broadcastStatus) return true
    MessageSender.sendMessage(sender, REQUEST_FAIL_GENERIC, true)
    return true
  }
}