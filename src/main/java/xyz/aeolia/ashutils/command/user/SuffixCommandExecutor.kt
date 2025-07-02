package xyz.aeolia.ashutils.command.user

import net.luckperms.api.model.group.Group
import net.luckperms.api.node.Node
import net.luckperms.api.node.types.SuffixNode
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.StringUtil
import xyz.aeolia.ashutils.instance.Message
import xyz.aeolia.ashutils.manager.PermissionManager
import xyz.aeolia.ashutils.manager.UserMapManager
import xyz.aeolia.ashutils.menu.SuffixMenu
import xyz.aeolia.ashutils.sender.MessageSender
import java.util.*
import java.util.function.Consumer

class SuffixCommandExecutor(var plugin: JavaPlugin) : TabExecutor {
  override fun onCommand(
    sender: CommandSender, command: Command,
    label: String, args: Array<String>
  ): Boolean {
    if (args.isEmpty()) {
      if (sender is Player) {
        SuffixMenu(plugin).INVENTORY.open(sender)
        return true
      } else {
        MessageSender.sendMessage(sender, "This command can only be executed by a player without an argument.")
        return true
      }
    }
    if (!sender.hasPermission("ashutils.suffix-grant")) {
      if (sender is Player) SuffixMenu(plugin).INVENTORY.open(sender)
      return true
    }

    val suffixList = plugin.config.getStringList("suffix.list")

    if (args[0] == "create") {
      if (!sender.hasPermission("ashutils.suffix-create")) return true
      val formatted = SuffixMenu.formatSuffix(args[1], true)
      PermissionManager.api.groupManager.createAndLoadGroup(args[1])
        .thenAccept(Consumer { e: Group? ->
          val node: Node = SuffixNode.builder()
            .priority(5)
            .suffix(" $formatted")
            .build()
          e!!.data().add(node)
          suffixList.add(args[1])
          if (!suffixList.contains(args[1])) {
            plugin.config.set("suffix.list", suffixList)
            plugin.saveConfig()
          }
          MessageSender.sendMessage(sender, "Created suffix $formatted<reset>.")
        })
      return true
    }
    if (args.size != 3) return invEx(sender)
    val status: Boolean = when (args[0]) {
      "grant" -> true
      "revoke" -> false
      else -> return invEx(sender)
    }
    val uuid = UserMapManager.getUserFromName(args[1])
    if (uuid == null) {
      MessageSender.sendMessage(sender, Message.player.notFound)
      return true
    }

    if (!suffixList.contains(args[2])) {
      MessageSender.sendMessage(sender, "Invalid suffix!")
      return true
    }
    if (PermissionManager.permissionUpdate(uuid, "ashutils.suffix." + args[2], status)) {
      if (!status) {
        PermissionManager.groupUpdate(plugin, uuid, args[2], false)
      }
      MessageSender.sendMessage(
        sender, ("Successfully " + (if (status) "granted " else "revoked ")
                + args[2] + " for " + args[1] + ".")
      )
      return true
    }
    MessageSender.sendMessage(
      sender, "Something went wrong trying to modify permissions. " +
              "Please check the console."
    )
    return true
  }

  fun invEx(sender: CommandSender): Boolean {
    // Short for invalid execution
    MessageSender.sendMessage(sender, "Invalid usage! Usage: " + "\n" + "/suffix [grant/revoke] <user> <suffix>")
    return true
  }

  override fun onTabComplete(
    sender: CommandSender, command: Command,
    label: String, args: Array<String>
  ): MutableList<String?>? {
    val completions: MutableList<String> = ArrayList<String>()
    val commands: MutableList<String> = ArrayList<String>()

    if (!sender.hasPermission("ashutils.suffix-grant")) return mutableListOf()

    if (args.size == 1) {
      commands.add("grant")
      commands.add("revoke")
      StringUtil.copyPartialMatches<MutableList<String>>(args[0], commands, completions)
    } else if (args.size == 2) {
      if (args[0] == "grant" || args[0] == "revoke") {
        for (p in Bukkit.getOnlinePlayers()) commands.add(p.name)
      }
      StringUtil.copyPartialMatches<MutableList<String>>(args[1], commands, completions)
    } else if (args.size == 3) {
      if (args[0] == "grant" || args[0] == "revoke") {
        commands.addAll(plugin.config.getStringList("suffix.list"))
        StringUtil.copyPartialMatches<MutableList<String>>(args[2], commands, completions)
      }
    }
    val returner: MutableList<String?>?
    completions.sort()
    if (completions.isEmpty()) {
      returner = null
    } else {
      @Suppress("UNCHECKED_CAST")
      returner = completions as MutableList<String?>?
    }
    return returner
  }
}