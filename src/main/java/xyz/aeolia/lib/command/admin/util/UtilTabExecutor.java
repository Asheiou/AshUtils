package xyz.aeolia.lib.command.admin.util;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import xyz.aeolia.lib.sender.MessageSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static xyz.aeolia.lib.utils.Message.Generic.COMMAND_USAGE;

public class UtilTabExecutor implements TabExecutor {
  JavaPlugin plugin;

  public UtilTabExecutor(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
    if (args.length == 0) {
      MessageSender.sendMessage(sender, "AeoliaLib v" + plugin.getDescription().getVersion() + " enabled.", true);
      return true;
    }
    String[] subCommandArgs = Arrays.copyOfRange(args, 1, args.length);
    return switch (args[0]) {
      //----------------Reload----------------//
      case "reload" -> ReloadHandler.doUtilReload(sender, plugin);
      //------------RestartOnEmpty-----------//
      case "restartonempty", "roe", "lc", "lockchat" ->
              StatusToggleHandler.doToggleStatus(sender, subCommandArgs, args[0]);
      case "clearchat", "cc" -> ClearChatHandler.doClearChat();
      case "motd" -> MotdHandler.handleCommand(sender, subCommandArgs);
      default -> {
        MessageSender.sendMessage(sender, COMMAND_USAGE, true);
        MessageSender.sendMessage(sender,"/util reload/restartonempty/motd/clearchat/lockchat.", false);
        yield true;
      }
    };
  }

  @Override
  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
    List<String> completions = new ArrayList<>();
    java.util.List<String> commands = new ArrayList<>();

    if (args.length == 1) {
      if (sender.hasPermission("lib.admin")) {
        commands.add("motd");
        commands.add("reload");
        commands.add("restartonempty");
        commands.add("lockchat");
        commands.add("clearchat");
      }
      StringUtil.copyPartialMatches(args[0], commands, completions);

    } else if (args.length == 2) {
      if (args[0].equals("restartonempty") || args[0].equals("lockchat")) {
        if (sender.hasPermission("lib.admin")) {
          commands.add("true");
          commands.add("false");
          commands.add("status");
        }
      }
      StringUtil.copyPartialMatches(args[1], commands, completions);
    }
    Collections.sort(completions);
    return completions;
  }
}
