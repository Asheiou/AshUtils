package xyz.aeolia.ashutils.command.admin;

import com.earth2me.essentials.Essentials;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.aeolia.ashutils.sender.Message;
import xyz.aeolia.ashutils.sender.MessageSender;
import xyz.aeolia.ashutils.user.User;
import xyz.aeolia.ashutils.user.UserHelper;

public class ModCommandExecutor implements CommandExecutor {
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player player)) {
      MessageSender.sendMessage(sender, Message.generic.notPlayer);
      return true;
    }
    User user = UserHelper.getUser(player);
    boolean toSet;
    if (args.length == 0) {
      toSet = !user.getModMode();
    } else switch (args[0]) {
      case "true":
        toSet = true;
        break;
      case "false":
        toSet = false;
        break;
      case "status":
        MessageSender.sendMessage(sender, "Mod mode is currently " + (user.getModMode()
                ? "enabled." : "disabled."));
        return true;
      default:
        MessageSender.sendMessage(sender, Message.generic.commandUsage);
        return false;
    }
    user.setModMode(toSet);
    MessageSender.sendMessage(sender, "Mod mode " + (toSet ? "enabled." : "disabled."));

    try {
      /* Soft depend ess */
      Essentials ess = (Essentials) Bukkit.getPluginManager().getPlugin("essentials");
      assert ess != null;
      ess.getUser(player).setSocialSpyEnabled(toSet);
    } catch (NoClassDefFoundError ignored) {
    }

    return true;
  }
}
