package xyz.aeolia.lib.task

import org.bukkit.command.CommandSender
import org.bukkit.scheduler.BukkitRunnable
import xyz.aeolia.lib.sender.MessageSender

class MessageLaterTask(val recipient: CommandSender, val message: String) : BukkitRunnable() {
  override fun run() {
    MessageSender.sendMessage(recipient, message)
  }
}