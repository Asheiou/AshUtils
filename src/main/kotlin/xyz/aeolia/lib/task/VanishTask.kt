package xyz.aeolia.lib.task

import net.ess3.api.events.VanishStatusChangeEvent
import org.bukkit.scheduler.BukkitRunnable
import xyz.aeolia.lib.manager.UserManager

class VanishTask(val event: VanishStatusChangeEvent) : BukkitRunnable() {
  override fun run() {
    UserManager.getUser(event.affected.base).vanish = event.value;
  }
}