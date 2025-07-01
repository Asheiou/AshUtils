package xyz.aeolia.ashutils.listener;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.aeolia.ashutils.manager.EconManager;
import xyz.aeolia.ashutils.sender.MessageSender;
import xyz.aeolia.ashutils.object.User;
import xyz.aeolia.ashutils.manager.UserManager;

public class VaultListener implements Listener {
  private final JavaPlugin plugin;

  public VaultListener(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onPlayerDeath(PlayerDeathEvent event) {
    FileConfiguration config = plugin.getConfig();
    Player victim = event.getEntity();
    if (!config.getStringList("pvp.worlds").contains(victim.getLocation().getWorld().getName())) {
      return;
    }
    for (ItemStack i : event.getDrops()) i.setType(Material.AIR);
    Player killer = event.getEntity().getKiller();
    if (killer == null) {
      return;
    }
    User victimuser = UserManager.getUser(victim);
    long currentTimeSeconds = System.currentTimeMillis() / 1000L;
    if (currentTimeSeconds - victimuser.getLastPvpPayout() < config.getLong("pvp.prize-cooldown")) {
      return;
    }
    long pvpPrize = config.getLong("pvp.kill-prize");
    EconManager.getEcon().depositPlayer(killer, pvpPrize);
    victimuser.setLastPvpPayout(currentTimeSeconds);

    String messageEnd = " and claimed a prize of <aqua>"
            + config.getString("currency-symbol") + pvpPrize + "</aqua>.";
    MessageSender.sendMessage(victim, killer.getName() + " killed you" + messageEnd);
    MessageSender.sendMessage(killer, "You killed " + victim.getName() + messageEnd);
  }
}
