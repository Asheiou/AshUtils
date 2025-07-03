package xyz.aeolia.ashutils.task

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import xyz.aeolia.ashutils.manager.UserManager

class UserPruneTask(val player: Player) : BukkitRunnable() {
  override fun run() {
    if (UserManager.getUser(player).online) return
    UserManager.removeUser(player)
    Bukkit.getLogger().info("[AshUtils] Pruned user ${player.name}")
  }
}