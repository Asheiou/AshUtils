package cymru.asheiou.ashutils.command.ashutils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

import java.util.List;

import cymru.asheiou.ashutils.sender.MessageSender;

public class AshUtilsTabExecutor implements TabExecutor {
  JavaPlugin plugin;
  
  public AshUtilsTabExecutor(JavaPlugin plugin) { this.plugin = plugin; }
  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) { 
    if (args.length == 0) {
      MessageSender.sendMessage(sender, "AshUtils v" + plugin.getDescription().getVersion() + " enabled.");
      return true;
    }
    switch (args[0]) {
    //----------------Reload----------------//
    case "reload":
      return ReloadHandler.doUtilReload(sender, plugin);
    //------------RestartOnEmpty-----------//
    case "restartonempty":
    case "lockchat":
      String instance = args[0];
      args = Arrays.copyOfRange(args, 1, args.length);
      return StatusToggleHandler.doToggleStatus(sender, args, instance);

      case "clearchat":
        return ClearChatHandler.doClearChat(plugin);

    default:
      MessageSender.sendMessage(sender,
          "Unrecognised subcommand. Expected: reload, restartonempty, clearchat, lockchat.");
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
        commands.add("lockchat");
        commands.add("clearchat");
      }
      StringUtil.copyPartialMatches(args[0], commands, completions);

    } else if (args.length == 2) {
      if (args[0].equals("restartonempty") || args[0].equals("lockchat")) {
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
