package cymru.asheiou.ashutils.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import cymru.asheiou.ashutils.restartonempty.QuitTask;

public class BukkitEventListener implements Listener {

  private final JavaPlugin plugin;

  public BukkitEventListener(JavaPlugin plugin) { this.plugin = plugin; }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerQuit(PlayerQuitEvent event) { new QuitTask().runTaskLater(this.plugin, 3); }
}
