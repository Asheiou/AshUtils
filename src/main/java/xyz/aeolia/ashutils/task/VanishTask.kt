package xyz.aeolia.ashutils.task

import net.ess3.api.events.VanishStatusChangeEvent
import org.bukkit.scheduler.BukkitRunnable
import xyz.aeolia.ashutils.manager.UserManager

class VanishTask(val event: VanishStatusChangeEvent) : BukkitRunnable() {
  override fun run() {
    UserManager.getUser(event.affected.uuid).vanish = event.value;
  }
}