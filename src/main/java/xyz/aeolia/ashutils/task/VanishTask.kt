package xyz.aeolia.ashutils.task

import net.ess3.api.events.VanishStatusChangeEvent
import org.bukkit.scheduler.BukkitRunnable
import xyz.aeolia.ashutils.user.UserHelper

class VanishTask(val event: VanishStatusChangeEvent) : BukkitRunnable() {
  override fun run() {
    UserHelper.getUser(event.affected.uuid).vanish = event.value;
  }
}