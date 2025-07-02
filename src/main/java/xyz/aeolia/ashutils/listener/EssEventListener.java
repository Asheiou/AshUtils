package xyz.aeolia.ashutils.listener;

import com.earth2me.essentials.Essentials;
import net.ess3.api.events.AfkStatusChangeEvent;
import net.ess3.api.events.VanishStatusChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.aeolia.ashutils.sender.WebhookSender;
import xyz.aeolia.ashutils.task.VanishTask;
import xyz.aeolia.ashutils.manager.UserManager;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EssEventListener implements Listener {
  JavaPlugin plugin;
  final Essentials ess;
  public EssEventListener(JavaPlugin plugin) {
    this.plugin = plugin;
    this.ess = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
    if (this.ess == null) {
      plugin.getLogger().severe("Essentials not found! This code has been initialised incorrectly.");
    }
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onAfkStatusChange(AfkStatusChangeEvent event) {
    if (UserManager.getUser(event.getAffected().getBase()).getVanish()) return;
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

  @EventHandler(priority = EventPriority.LOWEST)
  public void onVanishStatusChange(VanishStatusChangeEvent event) {
    new VanishTask(event).runTaskLater(plugin, 2);
  }
}
