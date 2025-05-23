package uk.asheiou.ashutils.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

import java.util.List;

import uk.asheiou.ashutils.AshUtils;
import uk.asheiou.ashutils.MessageSender;
import uk.asheiou.ashutils.restartonempty.ROEToggle;

public class AshUtilsTabExecutor implements TabExecutor {
  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    JavaPlugin plugin = JavaPlugin.getProvidingPlugin(AshUtils.class);
    
    if (args.length == 0) {
      MessageSender.sendMessage(sender, "AshUtils v" + plugin.getDescription().getVersion() + " enabled.");
      return true;
    }
    switch (args[0]) {
    case "reload":
      MessageSender.sendMessage(sender, "Starting config reload...");
      plugin.reloadConfig();
      MessageSender.sendMessage(sender, "Reload complete!");
      return true;

    case "restartonempty":
    case "roe":
      args = Arrays.copyOfRange(args, 1, args.length);
      if (args.length > 0) {
        if (args.length > 1) {
          MessageSender.sendMessage(sender, "Too many arguments! Usage:");
          return false;
        }
        switch (args[0].toLowerCase()) {
        case "true":
          ROEToggle.setStatus(true);
          MessageSender.sendMessage(sender, "The server will restart on empty.");
          return true;
        case "false":
          ROEToggle.setStatus(false);
          MessageSender.sendMessage(sender, "The server will no longer restart on empty.");
          return true;
        case "status":
          MessageSender.sendMessage(sender,
              "The server " + (ROEToggle.getStatus() ? "will" : "will not") + " restart on empty.");
          return true;
        default:
          MessageSender.sendMessage(sender, "Argument not recognised! Usage:");
          return false;
        }
      }
      boolean toSet = !ROEToggle.getStatus();
      ROEToggle.setStatus(toSet);
      MessageSender.sendMessage(sender,
          "The server " + (toSet ? "will" : "will no longer") + " restart on empty.");
      return true;

    default:
      MessageSender.sendMessage(sender,
          "Unrecognised subcommand. Usage: /ashutils restartonempty [true/false/status]");
      return true;
    }
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
    List<String> completions = new ArrayList<>();
    java.util.List<String> commands = new ArrayList<>();

    if (args.length == 1) {
      if (sender.hasPermission("ashutils.admin")) {
        commands.add("reload");
        commands.add("restartonempty");
      }
      StringUtil.copyPartialMatches(args[0], commands, completions);

    } else if (args.length == 2) {
      if (args[0].equals("restartonempty") || args[0].equals("roe")) {
        if (sender.hasPermission("ashutils.admin"))
          commands.add("true");
        commands.add("false");
        commands.add("status");
      }
      StringUtil.copyPartialMatches(args[1], commands, completions);
    }
    Collections.sort(completions);
    return completions;
  }

}
