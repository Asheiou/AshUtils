package xyz.aeolia.ashutils.listener;

import net.ess3.api.events.AfkStatusChangeEvent;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.aeolia.ashutils.sender.WebhookSender;
import xyz.aeolia.ashutils.user.UserHelper;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EssEventListener implements Listener {
  JavaPlugin plugin;

  public EssEventListener(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onAfkStatusChange(AfkStatusChangeEvent event) {
    if (UserHelper.getUser((Player) event.getAffected()).getVanish()) return;
    Server server = this.plugin.getServer();
    Pattern colourpattern = Pattern.compile("§(.)");
    Matcher matcher = colourpattern.matcher(event.getAffected().getDisplayName());
    String clean = matcher.replaceAll("");
    URI uri;
    try {
      uri = new URI(Objects.requireNonNull(plugin.getConfig().getString("discord.afk-webhook")));
    } catch (URISyntaxException e) {
      plugin.getLogger().severe("Invalid discord.afk-webhook URI!");
      return;
    }

    HttpResponse<String> response = WebhookSender.postWebhook(uri, "**" + clean + "** is " + (event.getValue() ? "now" : "no longer") + " AFK.");

    if (response.statusCode() != 204) {
      plugin.getLogger().severe("afk-webhook returned: " + response.statusCode() + " " + response.body());
    }
  }
}
