package uk.asheiou.ashutils.command;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import uk.asheiou.ashutils.EconManager;
import uk.asheiou.ashutils.MessageSender;

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
          MessageSender.sendMessage(player, "Invalid usage! Usage:");
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
              MessageSender.sendMessage(player, "You don't have that many in your hand to sell!");
              return true;
            }
          } catch (NumberFormatException e) {
            MessageSender.sendMessage(player, "Invalid argument! Usage:");
            return false;
          }
        }

        int worth = this.plugin.getConfig().getInt("econ.head-worth") * amountToSell;
        player.getInventory().getItemInMainHand().setAmount(itemInHand.getAmount() - amountToSell);
        EconManager.getEcon().depositPlayer(player, worth);
        MessageSender.sendMessage(player,
            "You have sold " + ChatColor.AQUA + (amountToSell == 1 ? " head" : " heads") + ChatColor.RESET + " for "
                + ChatColor.AQUA + this.plugin.getConfig().getString("econ.currency-symbol") + worth + ChatColor.RESET
                + ".");
        return true;

      }
      MessageSender.sendMessage(player, "You're not holding a head!");
      return true;

    }
    MessageSender.sendMessage(sender, "This command cannot be run from the console.");
    return true;
  }

}
