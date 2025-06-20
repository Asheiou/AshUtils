package cymru.asheiou.ashutils.listener;

import cymru.asheiou.ashutils.manager.MessageSender;
import cymru.asheiou.ashutils.manager.StatusManager;
import cymru.asheiou.ashutils.manager.UserMapManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import cymru.asheiou.ashutils.task.QuitTask;

import java.util.UUID;
import java.util.regex.Pattern;

public class BukkitEventListener implements Listener {

  private final JavaPlugin plugin;
  private final Pattern pattern = Pattern.compile("^:[A-Z]{4,}$");

  public BukkitEventListener(JavaPlugin plugin) { this.plugin = plugin; }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerQuit(PlayerQuitEvent event) { new QuitTask().runTaskLater(this.plugin, 3); }

  @EventHandler(priority = EventPriority.HIGH)
  public void onChat(AsyncPlayerChatEvent event) {
    if(pattern.matcher(event.getMessage()).matches()) {
      MessageSender.sendMessage(event.getPlayer(), "To use commands, type /<command>.");
      event.setCancelled(true);
      return;
    }
    if (StatusManager.getStatus("lockchat") && !event.getPlayer().hasPermission("asheiou.lockchat.exempt")) {
      event.setCancelled(true);
      MessageSender.sendMessage(event.getPlayer(), "Chat has been locked by a moderator.");
    }
  }

  @EventHandler(priority=EventPriority.LOW)
  public void onPlayerJoin(PlayerJoinEvent event) {
    UUID refUUID = UserMapManager.getUserFromName(event.getPlayer().getName());
    if(refUUID != null) {
      if(refUUID.equals(event.getPlayer().getUniqueId())) {
        plugin.getLogger().info(event.getPlayer().getName() + " is already registered to users.json.");
        return;
      }
      plugin.getLogger().info(event.getPlayer().getName() + " is registered to users.json as another UUID! Correcting.");
    } else {
      plugin.getLogger().info(event.getPlayer().getName() + " is not registered to users.json. Adding them.");
    }
    UserMapManager.putUserInMap(event.getPlayer().getName(), event.getPlayer().getUniqueId());
  }
}
