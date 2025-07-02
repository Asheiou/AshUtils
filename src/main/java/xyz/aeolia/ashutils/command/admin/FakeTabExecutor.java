package xyz.aeolia.ashutils.command.admin;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import xyz.aeolia.ashutils.instance.Message;
import xyz.aeolia.ashutils.sender.MessageSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FakeTabExecutor implements TabExecutor {
  private final JavaPlugin plugin;

  public FakeTabExecutor(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (!(sender instanceof Player) && args.length == 1) {
      MessageSender.sendMessage(sender, Message.generic.notPlayerNoArgs);
      return true;
    }
    if (args.length == 0) {
      MessageSender.sendMessage(sender, Message.generic.commandUsage);
      return false;
    }

    String message;
    switch (args[0]) {
      case "quit":
      case "leave":
      case "q":
        message = plugin.getConfig().getString("quit-message");
        break;
      case "join":
      case "j":
        message = plugin.getConfig().getString("join-message");
        break;
      default:
        MessageSender.sendMessage(sender, Message.generic.commandUsage);
        return false;
    }
    assert message != null;

    if (args.length == 1) {
      message = message.replace("{USERNAME}", sender.getName());
    } else if (args.length == 2) {
      message = message.replace("{USERNAME}", args[1]);
    } else {
      MessageSender.sendMessage(sender, Message.generic.commandUsage);
      return false;
    }
    Component deserialized = MessageSender.miniMessage.deserialize(message);
    Bukkit.getServer().broadcast(deserialized);
    return true;
  }

  @Override
  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
    List<String> completions = new ArrayList<>();
    java.util.List<String> commands = new ArrayList<>();

    if (args.length == 1) {
      if (sender.hasPermission("ashutils.fake")) {
        commands.add("join");
        commands.add("quit");
      }
    }
    StringUtil.copyPartialMatches(args[0], commands, completions);
    Collections.sort(completions);
    return completions;
  }
}
