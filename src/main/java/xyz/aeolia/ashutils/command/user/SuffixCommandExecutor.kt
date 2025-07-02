package xyz.aeolia.ashutils.command.user;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.aeolia.ashutils.manager.PermissionManager;
import xyz.aeolia.ashutils.manager.UserMapManager;
import xyz.aeolia.ashutils.menu.SuffixMenu;
import xyz.aeolia.ashutils.sender.MessageSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class SuffixCommandExecutor implements TabExecutor {
  JavaPlugin plugin;

  public SuffixCommandExecutor(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                           @NotNull String label, @NotNull String[] args) {
    if (args.length == 0) {
      if (sender instanceof Player player) {
        new SuffixMenu(plugin).INVENTORY.open(player);
        return true;
      } else {
        MessageSender.sendMessage(sender, "This command can only be executed by a player without an argument.");
        return true;
      }
    }
    if (!sender.hasPermission("ashutils.suffix-grant")) {
      if (sender instanceof Player player) new SuffixMenu(plugin).INVENTORY.open(player);
      return true;
    }

    List<String> suffixList = plugin.getConfig().getStringList("suffix.list");

    if (args[0].equals("create")) {
      if (!sender.hasPermission("ashutils.suffix-create")) return true;
      String formatted = SuffixMenu.formatSuffix(args[1], true);
      Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp creategroup " + args[1]);
      Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp group " + args[1] + " meta setsuffix 5 \" "
              + SuffixMenu.formatSuffix(args[1], false) + "\"");
      suffixList.add(args[1]);
      plugin.getConfig().set("suffix.list", suffixList);
      plugin.saveConfig();
      MessageSender.sendMessage(sender, "Created suffix " + formatted + "</reset>.");
      return true;
    }
    if (args.length != 3) return invEx(sender);

    boolean status;
    switch (args[0]) {
      case "grant":
        status = true;
        break;
      case "revoke":
        status = false;
        break;
      default:
        return invEx(sender);
    }
    UUID uuid = UserMapManager.getUserFromName(args[1]);
    if (uuid == null) {
      MessageSender.sendMessage(sender, "User not found!");
      return true;
    }

    if (!suffixList.contains(args[2])) {
      MessageSender.sendMessage(sender, "Invalid suffix!");
      return true;
    }
    if (PermissionManager.permissionUpdate(uuid, "ashutils.suffix." + args[2], status)) {
      if (!status) {
        PermissionManager.groupUpdate(plugin, uuid, args[2], false);
      }
      MessageSender.sendMessage(sender, "Successfully " + (status ? "granted " : "revoked ")
              + args[2] + " for " + args[1] + ".");
      return true;
    }
    MessageSender.sendMessage(sender, "Something went wrong trying to modify permissions. " +
            "Please check the console.");
    return true;
  }

  public boolean invEx(CommandSender sender) {
    // Short for invalid execution
    MessageSender.sendMessage(sender, "Invalid usage! Usage: " + "\n" + "/suffix [grant/revoke] <user> <suffix>");
    return true;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                              @NotNull String label, @NotNull String[] args) {
    List<String> completions = new ArrayList<>();
    java.util.List<String> commands = new ArrayList<>();

    if (!sender.hasPermission("ashutils.suffix-grant")) return List.of();

    if (args.length == 1) {
      commands.add("grant");
      commands.add("revoke");
      StringUtil.copyPartialMatches(args[0], commands, completions);

    } else if (args.length == 2) {
      if (args[0].equals("grant") || args[0].equals("revoke")) {
        for (Player p : Bukkit.getOnlinePlayers()) commands.add(p.getName());
      }
      StringUtil.copyPartialMatches(args[1], commands, completions);

    } else if (args.length == 3) {
      if (args[0].equals("grant") || args[0].equals("revoke")) {
        commands.addAll(plugin.getConfig().getStringList("suffix.list"));
        StringUtil.copyPartialMatches(args[2], commands, completions);
      }
    }
    Collections.sort(completions);
    return completions;
  }
}
