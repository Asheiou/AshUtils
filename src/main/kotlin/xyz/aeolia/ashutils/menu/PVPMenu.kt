package xyz.aeolia.ashutils.menu

import cymru.asheiou.inv.ClickableItem
import cymru.asheiou.inv.SmartInventory
import cymru.asheiou.inv.content.InventoryContents
import cymru.asheiou.inv.content.InventoryProvider
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.plugin.java.JavaPlugin
import xyz.aeolia.ashutils.manager.KitManager
import java.io.File
import kotlin.math.ceil

open class PVPMenu(open val plugin: JavaPlugin) : InventoryProvider {
  val INVENTORY = SmartInventory.builder()
    .id("pvpMenu")
    .provider(this)
    .size(getRows(), 9)
    .type(InventoryType.CHEST)
    .title("Kits")
    .build()

  override fun init(player: Player, contents: InventoryContents) {
    KitManager.kits.forEach { kit ->
      if(kit.value.id == "__global__") return@forEach
      val condition = try {
        player.hasPermission(kit.value.permission)
      } catch (e: NullPointerException) {
        true
      }
      if (condition) contents.add(ClickableItem.of(kit.value.displayItem.stack) { e ->
        e.isCancelled = true
        KitManager.givePlayerKit(player, kit.value)
      })
    }
  }

  fun getRows(): Int {
    val folder = File(plugin.dataFolder, "kits")
    var fileCount = folder.listFiles().size
    folder.listFiles().forEach { file ->
      if (file.isFile) if (file.nameWithoutExtension == "__global__") fileCount -= 1
    }
    return ceil(fileCount / 9.toDouble()).toInt()
  }
}