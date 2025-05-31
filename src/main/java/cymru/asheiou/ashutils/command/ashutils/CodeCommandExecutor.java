package cymru.asheiou.ashutils.command.ashutils;

import cymru.asheiou.ashutils.manager.MessageSender;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class CodeCommandExecutor implements CommandExecutor {
  JavaPlugin plugin;
  public CodeCommandExecutor(JavaPlugin plugin) {
    this.plugin = plugin;
  }
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (!(sender instanceof Player player)) { MessageSender.sendMessage(sender, "You must be a player to use this command."); return true; }
    String code = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
    MessageSender.sendMessage(sender, "Your code is " + ChatColor.AQUA + code + ChatColor.RESET + ". Staff may ask you for this code to verify you own your account.");
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString("discord-staffchat-cmd") + " User " + player.getName() + " (`" + player.getUniqueId() + "`) ran /code. Their code is " + code + ". Only accept this code within 24 hours of this message.");
    return true;
  }
}
