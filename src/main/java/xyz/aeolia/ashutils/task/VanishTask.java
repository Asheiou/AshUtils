package xyz.aeolia.ashutils.task;

import net.ess3.api.events.VanishStatusChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.aeolia.ashutils.user.UserHelper;

public class VanishTask extends BukkitRunnable {
  VanishStatusChangeEvent event;
  public VanishTask(VanishStatusChangeEvent e) {
    event = e;
  }

  @Override
  public void run() {
    UserHelper.getUser(event.getAffected().getUUID()).setVanish(event.getValue());
  }
}
