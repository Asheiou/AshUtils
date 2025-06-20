package cymru.asheiou.ashutils.command;

import com.google.gson.JsonObject;
import cymru.asheiou.ashutils.manager.MessageSender;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

public class ReportCommandExecutor implements CommandExecutor {
  JavaPlugin plugin;
  public ReportCommandExecutor(JavaPlugin plugin) {
    this.plugin = plugin;
  }
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
    if (args.length == 0) {
      MessageSender.sendMessage(sender, "Invalid usage! Usage:");
      return false;
    }
    String message = String.join(" ", args);
    HttpRequest.Builder request_builder;
    try {
      request_builder = HttpRequest.newBuilder(new URI(Objects.requireNonNull(plugin.getConfig().getString("discord.report-webhook"))));
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
    JsonObject json = new JsonObject();
    json.addProperty("content", sender.getName() + " has created a report:\n" + message);

    request_builder.POST(HttpRequest.BodyPublishers.ofString(json.toString()));
    request_builder.headers("content-type", "application/json");
    HttpRequest request = request_builder.build();
    HttpResponse<String> response = HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString()).join();
    if(response.statusCode() != 200) {
      plugin.getLogger().warning("Could not send report: " + response.body());
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
