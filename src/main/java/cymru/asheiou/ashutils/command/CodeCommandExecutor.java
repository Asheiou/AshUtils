package cymru.asheiou.ashutils.command;

import cymru.asheiou.ashutils.sender.MessageSender;
import cymru.asheiou.ashutils.sender.WebhookSender;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.Objects;

public class CodeCommandExecutor implements CommandExecutor {
  JavaPlugin plugin;
  public CodeCommandExecutor(JavaPlugin plugin) {
    this.plugin = plugin;
  }
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (!(sender instanceof Player player)) { MessageSender.sendMessage(sender, "You must be a player to use this command."); return true; }
    String code = RandomStringUtils.randomAlphanumeric(6).toUpperCase();

    URI uri;
    try {
      uri = new URI(Objects.requireNonNull(plugin.getConfig().getString("discord.code-webhook")));
    } catch (URISyntaxException e) {
      plugin.getLogger().severe("discord.code-webhook URI invalid. Please check your config.");
      e.printStackTrace();
      MessageSender.sendMessage(sender, "An internal error occurred. Please contact an administrator");
      return true;
    }

    HttpResponse<String> response = WebhookSender.postWebhook(uri, player.getName() + "'s code is **" + code + "**. Only accept it within 24 hours of this message.");
    if(response.statusCode() != 204) {
      plugin.getLogger().severe("discord.code-webhook returned " + response.statusCode() + " " + response.body());
      MessageSender.sendMessage(sender, "An internal error occurred. Please contact an administrator");
    }

    MessageSender.sendMessage(sender, "Your code is " + ChatColor.AQUA + code + ChatColor.RESET + ". Staff may ask you for this code to verify you own your account. It is valid for 24 hours.");
    return true;
  }
}
