package xyz.aeolia.lib.command.admin

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import xyz.aeolia.lib.utils.Message.Error.CONFIG
import xyz.aeolia.lib.utils.Message.Generic.COMMAND_USAGE
import xyz.aeolia.lib.utils.Message.Generic.NOT_PLAYER
import xyz.aeolia.lib.utils.Message.Player.NOT_FOUND
import xyz.aeolia.lib.utils.Message.Player.OFFLINE
import xyz.aeolia.lib.manager.UserManager
import xyz.aeolia.lib.manager.UserMapManager
import xyz.aeolia.lib.sender.MessageSender

class MiniMessageCommandExecutor(val plugin: JavaPlugin) : CommandExecutor {
  override fun onCommand(
    sender: CommandSender,
    command: Command,
    label: String,
    argsInput: Array<out String>
  ): Boolean {
    var args = argsInput
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
        args = args.drop(1).toTypedArray()
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
      MessageSender.sendMessage(sender, String.format(CONFIG, "chat-format"))
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