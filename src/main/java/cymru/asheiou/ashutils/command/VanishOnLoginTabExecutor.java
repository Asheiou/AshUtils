package cymru.asheiou.ashutils.command;

import cymru.asheiou.ashutils.manager.PermissionManager;
import cymru.asheiou.ashutils.sender.MessageSender;
import cymru.asheiou.ashutils.manager.LuckPermsManager;
import cymru.asheiou.ashutils.manager.UserMapManager;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class VanishOnLoginTabExecutor implements TabExecutor {
  private final JavaPlugin plugin;
  public VanishOnLoginTabExecutor(JavaPlugin plugin) {
    this.plugin = plugin;
  }
  @Override
  @SuppressWarnings("deprecated")
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    switch (args.length) {
      case 0:
        if (sender instanceof Player player) {
          return permissionUpdate(sender, player.getUniqueId(), player.getName(), !player.hasPermission("group." + plugin.getConfig().getString("vanish-on-login-group")));
        }
        MessageSender.sendMessage(sender, "This command cannot be run from the console without arguments.");
      case 1:
        if (sender instanceof Player player) {
          switch (args[0]) {
            case "true":
              return permissionUpdate(sender, player.getUniqueId(), player.getName(), true);
            case "false":
              return permissionUpdate(sender, player.getUniqueId(), player.getName(), false);
            default:
              MessageSender.sendMessage(sender, "Unknown argument! Usage:");
              return false;
          }
        }
        case 2:
          if(sender.hasPermission("ashutils.vanishonlogin.others")) {
            UUID playerUUID = UserMapManager.getUserFromName(args[1]);
            if (playerUUID == null) {
              MessageSender.sendMessage(sender, "Player not found. Check your spelling and try again.");
              return true;
            }
            switch (args[0]) {
              case "true":
                return permissionUpdate(sender, playerUUID, args[1], true);
              case "false":
                return permissionUpdate(sender, playerUUID, args[1], false);
              default:
                MessageSender.sendMessage(sender, "Unknown argument! Usage:");
                return false;
            }
          }
      default:
        MessageSender.sendMessage(sender, "Too many arguments! Usage:");
        return false;
    }
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    List<String> completions = new ArrayList<>();
    java.util.List<String> commands = new ArrayList<>();

    if (args.length == 1) {
      if (sender.hasPermission("ashutils.vanishonlogin")) {
        commands.add("true");
        commands.add("false");
      }
      StringUtil.copyPartialMatches(args[0], commands, completions);

    } else if (args.length == 2) {
      if (args[0].equals("true") || args[0].equals("false")) {
        if (sender.hasPermission("ashutils.vanishonlogin.others")) {
          for (Player player : Bukkit.getOnlinePlayers()) {
            commands.add(player.getName());
          }
        }
        StringUtil.copyPartialMatches(args[1], commands, completions);
      }
    }
    Collections.sort(completions);
    return completions;
  }

  boolean permissionUpdate(CommandSender sender, UUID uuid, String playerName, boolean status) {
    PermissionManager.permissionUpdate(plugin, uuid, plugin.getConfig().getString("vanish-on-login-group"), status);
    String toSend = "VanishOnLogin " + (status ? "enabled" : "disabled") + " for " + playerName+".";
    MessageSender.sendMessage(sender, toSend);
    return true;
  }
}
