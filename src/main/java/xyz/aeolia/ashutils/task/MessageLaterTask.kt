package xyz.aeolia.ashutils.task

import jdk.internal.joptsimple.internal.Messages.message
import org.bukkit.command.CommandSender
import org.bukkit.scheduler.BukkitRunnable
import xyz.aeolia.ashutils.sender.MessageSender

class MessageLaterTask(val recipient: CommandSender, val message: String) : BukkitRunnable() {
  override fun run() {
    MessageSender.sendMessage(recipient, message, true)
  }
}