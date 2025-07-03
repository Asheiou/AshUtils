package xyz.aeolia.ashutils.command.admin

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import xyz.aeolia.ashutils.instance.Message.Error.CONFIG
import xyz.aeolia.ashutils.instance.Message.Generic.COMMAND_USAGE
import xyz.aeolia.ashutils.instance.Message.Generic.NOT_PLAYER
import xyz.aeolia.ashutils.instance.Message.Player.NOT_FOUND
import xyz.aeolia.ashutils.instance.Message.Player.OFFLINE
import xyz.aeolia.ashutils.manager.UserManager
import xyz.aeolia.ashutils.manager.UserMapManager
import xyz.aeolia.ashutils.sender.MessageSender

class MiniMessageCommandExecutor(val plugin: JavaPlugin) : CommandExecutor {
  override fun onCommand(
    sender: CommandSender,
    command: Command,
    label: String,
    args: Array<out String>
  ): Boolean {
    if (sender !is Player) {
      MessageSender.sendMessage(sender, NOT_PLAYER, true)
      return true
    }
    if (args.isEmpty()) {
      MessageSender.sendMessage(sender, COMMAND_USAGE, true)
      return false
    }
    val mm = MessageSender.miniMessage

    val identity: Player
    if (args[0].startsWith('@')) {
      val sudo = args[0].substring(1)
      val uuid = UserMapManager.getUuidFromName(sudo) ?: run {
        MessageSender.sendMessage(sender, NOT_FOUND, true)
        return false
      }
      val user = UserManager.getUser(Bukkit.getOfflinePlayer(uuid))
      if (user.online) {
        identity = Bukkit.getPlayer(uuid) ?: run {
          MessageSender.sendMessage(sender, OFFLINE, true)
          return true
        }
        args.drop(0)
      } else {
        MessageSender.sendMessage(sender, OFFLINE, true)
        return true
      }
    } else {
      identity = sender
    }

    val message = mm.deserialize(args.joinToString(" "))
    var chatFormat = plugin.config.getString("chat-format")
    if (chatFormat == null) {
      MessageSender.sendMessage(sender, String.format(CONFIG, "chat-format"), true)
      return true
    }
    chatFormat = chatFormat.replace("{DISPLAYNAME}", mm.serialize(identity.displayName()))
    chatFormat += " "
    val toSend = Component.text()
      .append { mm.deserialize(chatFormat); }
      .append { message }
      .build()

    Bukkit.getServer().sendMessage(toSend)
    return true
  }
}