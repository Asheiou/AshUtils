package xyz.aeolia.ashutils.listener;

import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.aeolia.ashutils.manager.PermissionManager;
import xyz.aeolia.ashutils.menu.SuffixMenu;
import xyz.aeolia.ashutils.task.MessageLaterTask;

public class SuffixListener implements Listener {
  private final JavaPlugin plugin;

  public SuffixListener(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerJoin(PlayerJoinEvent event) {
    FileConfiguration config = plugin.getConfig();
    if (!config.getBoolean("suffix.ontime-reward.enabled")) return;
    Player player = event.getPlayer();
    long ontimeMillis = player.getStatistic(Statistic.PLAY_ONE_MINUTE) * 50L;
    double awardTimeMillis = config.getDouble("suffix.ontime-reward.reward-time") * 86400000L;
    if (ontimeMillis > awardTimeMillis) {
      String rewardGroup = config.getString("suffix.ontime-reward.name");
      if (rewardGroup == null) {
        plugin.getLogger().severe("Ontime reward group is null");
        return;
      }
      if (player.hasPermission("group." + rewardGroup)) return;
      PermissionManager.groupUpdate(plugin, player.getUniqueId(), rewardGroup, true);
      new MessageLaterTask(player, "You have received an ontime reward suffix: "
              + SuffixMenu.formatSuffix(rewardGroup) + ChatColor.RESET + "! You can equip it by running "
              + ChatColor.AQUA + "/suffix" + ChatColor.RESET + ".")
              .runTaskLater(plugin, 20L);
    }
  }
}
