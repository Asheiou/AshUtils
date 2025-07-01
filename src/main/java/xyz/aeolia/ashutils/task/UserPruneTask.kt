package xyz.aeolia.ashutils.task

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import xyz.aeolia.ashutils.user.UserHelper

class UserPruneTask(val player: Player) : BukkitRunnable() {
  override fun run() {
    if (player.isOnline) return
    UserHelper.removeUser(player.uniqueId)
    Bukkit.getLogger().info("[AshUtils] Pruned user ${player.name}")
  }
}