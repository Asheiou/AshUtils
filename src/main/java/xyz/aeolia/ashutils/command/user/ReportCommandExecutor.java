package xyz.aeolia.ashutils.command.user;

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

import static xyz.aeolia.ashutils.utils.Message.Error.GENERIC;
import static xyz.aeolia.ashutils.utils.Message.Generic.COMMAND_USAGE;
import static xyz.aeolia.ashutils.utils.Message.Generic.NOT_PLAYER;

public class ReportCommandExecutor implements CommandExecutor {
  JavaPlugin plugin;

  public ReportCommandExecutor(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
    if (!(sender instanceof Player player)) {
      MessageSender.sendMessage(sender, NOT_PLAYER, true);
      return true;
    }

    if (args.length == 0) {
      MessageSender.sendMessage(sender, COMMAND_USAGE, true);
      return false;
    }
    URI uri;
    try {
      uri = new URI(Objects.requireNonNull(plugin.getConfig().getString("discord.report-webhook")));
    } catch (Exception e) {
      plugin.getLogger().severe("Invalid discord.report-webhook in config.yml.");
      e.printStackTrace();
      MessageSender.sendMessage(sender, GENERIC, true);
      return true;
    }
    Location location = player.getLocation();
    HttpResponse<String> response = WebhookSender.postWebhook(uri, sender.getName() + " has created a report at " +
            location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + " in " +
            Objects.requireNonNull(location.getWorld()).getName() + ":\n" + String.join(" ", args));

    if (response.statusCode() != 204) {
      plugin.getLogger().severe("Could not send report: " + response.statusCode() + " " + response.body());
      MessageSender.sendMessage(sender, GENERIC, true);
      return true;
    }
    MessageSender.sendMessage(sender, "Report sent successfully. You will receive a response via " +
            "<aqua>/mail</aqua> or on our Discord if you're a member.", true);
    return true;
  }
}
