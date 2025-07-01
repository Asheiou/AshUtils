package xyz.aeolia.ashutils.command.admin.ashutils

import org.bukkit.command.CommandSender
import xyz.aeolia.ashutils.sender.MessageSender

class MotdHandler {
  companion object {
    @JvmStatic
    private var motd : String? = null

    @JvmStatic
    fun getMotd() : String?{
      return motd
    }

    @JvmStatic
    fun setMotd (motd: String?) {
      this.motd = motd
    }

    @JvmStatic
    fun handleCommand(sender: CommandSender, args: Array<out String?>?): Boolean {
      if (args?.size == 0 || args == null) {
        setMotd(null)
        MessageSender.sendMessage(sender, "MOTD reset!")
        return true
      }
      setMotd(args.joinToString(" "))
      MessageSender.sendMessage(sender, "Motd set to:")
      MessageSender.sendMessage(sender, motd)
      MessageSender.sendMessage(
        sender, "It will persist until the server restarts or you reset it by " +
                "running this command again without an argument."
      )
      return true
    }
  }
}