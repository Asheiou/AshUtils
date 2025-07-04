package xyz.aeolia.ashutils.instance

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.inventory.ItemStack
import xyz.aeolia.ashutils.sender.MessageSender
import java.util.Locale.getDefault

class Item(
  val displayName: String? = null,
  val lore: List<String>? = null,
  val material: String,
  val amount: Int,
  val enchantments: MutableMap<String, Int>? = null
) {
  val stack: ItemStack? = loadStack()

  @Suppress("UnstableApiUsage")
  fun loadStack(): ItemStack? {
    val mm = MessageSender.miniMessage

    val material: Material;
    try {
       material= Material.valueOf(this.material.uppercase(getDefault()))
    } catch(_: IllegalArgumentException) {
      Bukkit.getLogger().warning("Bad material: ${this.material}")
      return null;
    }
    val stack = ItemStack.of(material, this.amount)
    val meta = stack.itemMeta
    if (this.displayName != null) {
      meta.displayName(mm.deserialize(displayName))
    }

    if (this.lore != null) {
      val lore = mutableListOf<Component>()
      for (line in this.lore) {
        lore.add(mm.deserialize(line))
      }
      meta.lore(lore)
    }

    if(this.enchantments != null) {
      for (enchant in this.enchantments) {
        val enchantment = Registry.ENCHANTMENT.get(NamespacedKey.minecraft(enchant.key)) ?: run {
          Bukkit.getLogger().warning("Bad enchant key: ${enchant.key}")
          return null
        }
        meta.enchants.put(enchantment, enchant.value)
      }
    }
    stack.itemMeta = meta
    return stack
  }
}