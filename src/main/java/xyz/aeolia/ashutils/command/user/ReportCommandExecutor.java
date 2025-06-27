package xyz.aeolia.ashutils.command.user;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import xyz.aeolia.ashutils.sender.MessageSender;
import xyz.aeolia.ashutils.sender.WebhookSender;

import java.net.URI;
import java.net.http.HttpResponse;
import java.util.Objects;

public class ReportCommandExecutor implements CommandExecutor {
  JavaPlugin plugin;

  public ReportCommandExecutor(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
    if (!(sender instanceof Player player)) {
      MessageSender.sendMessage(sender, "This command can only be executed by a player.");
      return true;
    }

    if (args.length == 0) {
      MessageSender.sendMessage(sender, "Invalid usage! Usage:");
      return false;
    }
    URI uri;
    try {
      uri = new URI(Objects.requireNonNull(plugin.getConfig().getString("discord.report-webhook")));
    } catch (Exception e) {
      plugin.getLogger().severe("Invalid discord.report-webhook in config.yml.");
      e.printStackTrace();
      MessageSender.sendMessage(sender, "An internal error occurred. Please contact an administrator.");
      return true;
    }
    Location location = player.getLocation();
    HttpResponse<String> response = WebhookSender.postWebhook(uri, sender.getName() + " has created a report at " +
            location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + " in " +
            Objects.requireNonNull(location.getWorld()).getName() + ":\n" + String.join(" ", args));

    if (response.statusCode() != 204) {
      plugin.getLogger().severe("Could not send report: " + response.statusCode() + " " + response.body());
      MessageSender.sendMessage(sender, "Could not send report, error " + response.statusCode() +
              ". Please contact an administrator via " + ChatColor.AQUA + "/discord " + ChatColor.RESET
              + "or the email on " + ChatColor.AQUA + "/support" + ChatColor.RESET + ".");
      return true;
    }
    MessageSender.sendMessage(sender, "Report sent successfully. You will receive a response via " +
            ChatColor.AQUA + "/mail " + ChatColor.RESET + "or on our Discord if you're a member.");
    return true;
  }
}
