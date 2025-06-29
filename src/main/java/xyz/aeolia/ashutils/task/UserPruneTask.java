package xyz.aeolia.ashutils.task;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.aeolia.ashutils.user.UserHelper;

public class UserPruneTask extends BukkitRunnable {
  Player player;

  public UserPruneTask(Player player) {
    this.player = player;
  }

  @Override
  public void run() {
    if (player.isOnline()) return;
    UserHelper.removeUser(player.getUniqueId());
  }
}
