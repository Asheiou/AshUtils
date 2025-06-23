package cymru.asheiou.ashutils.listener;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cymru.asheiou.ashutils.sender.WebhookSender;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import net.ess3.api.events.AfkStatusChangeEvent;

public class EssEventListener implements Listener {
  JavaPlugin plugin;

  public EssEventListener(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onAfkStatusChange(AfkStatusChangeEvent event) {
    if (event.getAffected().isVanished()) return;
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
