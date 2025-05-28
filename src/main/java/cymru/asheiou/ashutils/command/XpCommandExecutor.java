package cymru.asheiou.ashutils.command;

import java.math.BigDecimal;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import cymru.asheiou.ashutils.manager.EconManager;
import cymru.asheiou.ashutils.manager.ExperienceManager;
import cymru.asheiou.ashutils.MessageSender;

public class XpCommandExecutor implements CommandExecutor {
  JavaPlugin plugin;
  ChatColor aqua = ChatColor.AQUA;
  ChatColor reset = ChatColor.RESET;
  Economy econ = EconManager.getEcon();

  public XpCommandExecutor(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player player)) {
      MessageSender.sendMessage(sender, "This command cannot be run from the console.");
      return true;
    }
    if (args.length > 1) {
      MessageSender.sendMessage(player, "Too many arguments! Usage:");
      return false;
    }
    String currencySymbol = plugin.getConfig().getString("currency-symbol");
    ExperienceManager experienceManager = new ExperienceManager(player);
    
    switch(command.getName()) {
    case "xpbuy":
      return buyXp(player, args[0], experienceManager, experienceManager.getTotalExperience(), currencySymbol);
    case "xpsell":
      return sellXp(player, args[0], experienceManager, experienceManager.getTotalExperience(), currencySymbol);
    default:
      MessageSender.sendMessage(player,
          "Error: Command not found. This is an issue with the plugin, please inform an administrator.");
      return false;
    }
  }

  public boolean buyXp(Player player, String arg, ExperienceManager experienceManager, int playerCurrentXp, String currencySymbol) {
    int xpToBuy;
    double playerBalance = econ.getBalance(player);
    double costPerXp = plugin.getConfig().getDouble("xp-buy-cost");
    int maximumXpPurchasable = (int) Math.floor(playerBalance / costPerXp);

    if(costPerXp > playerBalance) {
      MessageSender.sendMessage(player, "You need at least " + aqua + currencySymbol + costPerXp + reset + " to buy XP!");
      return true;
    }
    
    if (arg.equalsIgnoreCase("max") || arg.equalsIgnoreCase("maximum")) {
      xpToBuy = maximumXpPurchasable;
    } else
      try {
        xpToBuy = Integer.parseInt(arg);
      } catch (Exception e) {
        MessageSender.sendMessage(player, "Argument not recognised! Usage:");
        return false;
      }
    double totalCost = (BigDecimal.valueOf(costPerXp).multiply(BigDecimal.valueOf(xpToBuy))).doubleValue();
    if (totalCost > playerBalance) {
      MessageSender.sendMessage(player, "You don't have enough money for that! You can buy a maximum of " + aqua
          + maximumXpPurchasable + reset + " XP.");
    }
    int xpMaximumBuy = plugin.getConfig().getInt("xp-maximum-buy");
    if (xpToBuy > xpMaximumBuy && xpMaximumBuy > 0) {
      MessageSender.sendMessage(player, "This server limits the amount of XP you can buy per use of this command to "
          + aqua + xpMaximumBuy + reset + ".");
      xpToBuy = xpMaximumBuy;
    }
    econ.withdrawPlayer(player, totalCost);
    experienceManager.setTotalExperience(playerCurrentXp + xpToBuy);

    MessageSender.sendMessage(player, "You have bought " + aqua + xpToBuy + " XP" + reset + "@ " + aqua + currencySymbol
        + costPerXp + reset + " per XP for " + aqua + currencySymbol + totalCost + reset + ".");
    return true;
  }

  public boolean sellXp(Player player, String arg, ExperienceManager experienceManager, int playerCurrentXp, String currencySymbol) {
    if(playerCurrentXp==0) {
      MessageSender.sendMessage(player, "You have no XP to sell!");
      return true;
    }
    int xpToSell;
    if (arg.equalsIgnoreCase("all")) {
      xpToSell = playerCurrentXp;
    } else {
      try {
        xpToSell = Integer.parseInt(arg);
      } catch (Exception e) {
        MessageSender.sendMessage(player, "Argument not recognised! Usage:");
        return false;
      }
      if (xpToSell > playerCurrentXp) {
        MessageSender.sendMessage(player,
            "You don't have that much XP to sell! You can sell up to " + aqua + playerCurrentXp + reset + " XP.");
        return true;
      }
    }
    double worthPerXp = plugin.getConfig().getInt("xp-sell-worth");
    double totalWorth = (BigDecimal.valueOf(worthPerXp).multiply(BigDecimal.valueOf(xpToSell))).doubleValue();
    experienceManager.setTotalExperience(playerCurrentXp - xpToSell);
    econ.depositPlayer(player, totalWorth);
    MessageSender.sendMessage(player, "You have sold " + aqua + xpToSell + " XP " + reset + "@ " + aqua + currencySymbol
        + worthPerXp + reset + " per XP for " + aqua + currencySymbol + totalWorth + reset + ".");
    return true;
  }
}
