package cymru.asheiou.ashutils.command;

import cymru.asheiou.ashutils.manager.MessageSender;
import net.luckperms.api.messenger.message.Message;
import net.md_5.bungee.api.ChatColor;
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

public class FakeTabExecutor implements TabExecutor {
  private final JavaPlugin plugin;
  public FakeTabExecutor(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if(!(sender instanceof Player) && args.length==1) {
      MessageSender.sendMessage(sender, "This command can only be executed by a player without an argument.");
      return true;
    }
    if (args.length == 0) {
      MessageSender.sendMessage(sender,"Unrecognised command usage. Usage: ");
      return false;
    }

    String message = null;
    switch(args[0]) {
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
        MessageSender.sendMessage(sender, "Unrecognised command usage. Usage: ");
        return false;
    }
    if (args.length == 1) {
      message.replace("{USERNAME}", sender.getName());
    } else if (args.length == 2) {
      message.replace("{USERNAME}", args[1]);
    }
    else {
      MessageSender.sendMessage(sender, "Unrecognised command usage. Usage:");
      return false;
    }

    Bukkit.broadcastMessage(message.replace('&', ChatColor.COLOR_CHAR));
    return true;
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
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
