package xyz.aeolia.ashutils.command.user;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.aeolia.ashutils.manager.EconManager;
import xyz.aeolia.ashutils.instance.Message;
import xyz.aeolia.ashutils.sender.MessageSender;

public class HeadSellCommandExecutor implements CommandExecutor {
  JavaPlugin plugin;

  public HeadSellCommandExecutor(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (sender instanceof Player player) {
      ItemStack itemInHand = player.getInventory().getItemInMainHand();
      if (itemInHand.getType() == Material.PLAYER_HEAD) {
        if (args.length != 1) {
          MessageSender.sendMessage(player, Message.Generic.COMMAND_USAGE);
          return false;
        }

        String arg = args[0];
        int amountToSell;

        if (arg.equals("all")) {
          amountToSell = itemInHand.getAmount();
        } else {
          try {
            amountToSell = Integer.parseInt(arg);
            if (amountToSell > itemInHand.getAmount()) {
              MessageSender.sendMessage(player, Message.Econ.TOO_MANY);
              return true;
            }
          } catch (NumberFormatException e) {
            MessageSender.sendMessage(player, Message.Generic.COMMAND_USAGE);
            return false;
          }
        }

        int worth = this.plugin.getConfig().getInt("head-worth") * amountToSell;
        player.getInventory().getItemInMainHand().setAmount(itemInHand.getAmount() - amountToSell);
        EconManager.getEcon().depositPlayer(player, worth);
        MessageSender.sendMessage(player,
                "You have sold <aqua>" + amountToSell + (amountToSell == 1 ? " head" : " heads") + "</aqua> for <aqua>"
                        + this.plugin.getConfig().getString("currency-symbol") + worth + "</aqua>.");
        return true;

      }
      MessageSender.sendMessage(player, "You're not holding a head!");
      return true;

    }
    MessageSender.sendMessage(sender, Message.Generic.NOT_PLAYER);
    return true;
  }

}
