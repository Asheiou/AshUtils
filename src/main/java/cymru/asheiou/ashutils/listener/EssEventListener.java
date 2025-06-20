package cymru.asheiou.ashutils.listener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    Server server = this.plugin.getServer();
    Pattern colourpattern = Pattern.compile("§(.)");
    Matcher matcher = colourpattern.matcher(event.getAffected().getDisplayName());
    String clean = matcher.replaceAll("");
    server.dispatchCommand(server.getConsoleSender(), this.plugin.getConfig().getString("discord.broadcast-cmd") +
        " **" + clean + "** is " + (event.getValue() ? "now" : "no longer") + " AFK.");
  }
}
