package uk.asheiou.ashutils;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import uk.asheiou.ashutils.restartonempty.ROEQuitTask;

public class EventListener implements Listener {
  
  private final JavaPlugin plugin;
  
  public EventListener(JavaPlugin plugin) { this.plugin = plugin; }
  
  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerQuit(PlayerQuitEvent event) {
    new ROEQuitTask().runTaskLater(this.plugin, 3);
  }
}
