package xyz.aeolia.ashutils.manager

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import net.kyori.adventure.audience.Audience
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import xyz.aeolia.ashutils.instance.Kit
import xyz.aeolia.ashutils.instance.Message.Error.GENERIC
import xyz.aeolia.ashutils.sender.MessageSender
import java.io.File

class KitManager {
  companion object {
    val gson = Gson()
    val kits = mutableMapOf<String, Kit>()
    lateinit var plugin: JavaPlugin
    lateinit var scope: CoroutineScope
    var loaded = false

    @JvmStatic
    fun init(plugin: JavaPlugin, recipient: Audience? = null) {
      loaded = false
      this.plugin=plugin
      scope = CoroutineScope(Dispatchers.Default)
      scope.launch {
        initCoro(recipient)
        loaded=true
      }
    }

    private fun initCoro(recipient: Audience? = null) {
      for ((k, _) in kits) {
        kits.remove(k)
      }
      val folder = File(plugin.dataFolder, "kits")
      if (!folder.exists()) {
        folder.mkdirs()
      }
      folder.listFiles()?.forEach { file ->
        if (file.isFile) {
            try {
              val id = file.nameWithoutExtension
              val kit = file.reader().use { reader ->
                gson.fromJson(reader, Kit::class.java).apply {
                  this.id = id
                }
              }
              kits[id] = kit
            } catch (e: Exception) {
              plugin.logger.warning("Exception while reading ${file.name}: ${e.message}")
            }
        }
      }
      if(recipient != null) {
        MessageSender.sendMessage(recipient, "Kit reload complete! ${kits.size} kits loaded.")
      }
    }

    fun cleanup() {
      scope.cancel()
    }

    fun givePlayerKit(player: Player, kit: Kit) : Boolean {
      val items = kit.items.toMutableList()
      if (kits.containsKey("__global__")) {
        items.addAll(kits["__global__"]!!.items)
      }
      player.inventory.clear()
      for (item in kit.items) {
        val stack = item.stack ?: run {
          MessageSender.sendMessage(player, GENERIC)
          plugin.logger.warning("Error processing item $item in kit ${kit.id}")
          return false
        }
        if (!equipIfArmor(player, stack)) {
          player.inventory.addItem(stack)
        }
      }
      return true
    }

    fun equipIfArmor(player: Player, stack: ItemStack) : Boolean {
      val id = stack.type.name
      when {
        id.contains("HELMET") -> player.inventory.helmet = stack
        id.contains("CHESTPLATE") -> player.inventory.chestplate = stack
        id.contains("LEGGINGS") -> player.inventory.leggings = stack
        id.contains("BOOTS") -> player.inventory.boots = stack
        id.contains("SHIELD") -> player.inventory.setItemInOffHand(stack)
        else -> return false
      }
      return true
    }
  }
}