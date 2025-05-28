package cymru.asheiou.ashutils.listener;

import cymru.asheiou.ashutils.MessageSender;
import cymru.asheiou.ashutils.manager.StatusManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import cymru.asheiou.ashutils.task.QuitTask;

public class BukkitEventListener implements Listener {

  private final JavaPlugin plugin;

  public BukkitEventListener(JavaPlugin plugin) { this.plugin = plugin; }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerQuit(PlayerQuitEvent event) { new QuitTask().runTaskLater(this.plugin, 3); }

  @EventHandler(priority = EventPriority.HIGH)
  public void onChat(AsyncPlayerChatEvent event) {
    if(event.getMessage().startsWith(":") && event.getMessage().equals(event.getMessage().toUpperCase())) {
      MessageSender.sendMessage(event.getPlayer(), "To use commands, type /<command>.");
      event.setCancelled(true);
      return;
    }
    if (StatusManager.getStatus("lockchat") && !event.getPlayer().hasPermission("asheiou.lockchat.exempt")) {
      event.setCancelled(true);
      MessageSender.sendMessage(event.getPlayer(), "Chat has been locked by a moderator.");
    }
  }
}
