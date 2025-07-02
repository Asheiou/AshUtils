package xyz.aeolia.ashutils.menu;


import cymru.asheiou.ClickableItem;
import cymru.asheiou.SmartInventory;
import cymru.asheiou.content.InventoryContents;
import cymru.asheiou.content.InventoryProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.aeolia.ashutils.manager.PermissionManager;
import xyz.aeolia.ashutils.sender.MessageSender;

import java.util.List;

public class SuffixMenu implements InventoryProvider {
  public final SmartInventory INVENTORY;
  private final JavaPlugin plugin;

  public SuffixMenu(JavaPlugin plugin) {
    this.plugin = plugin;
    INVENTORY = SmartInventory.builder()
            .id("suffixGui")
            .provider(this)
            .size(plugin.getConfig().getInt("suffix.size"), 9)
            .title("Suffix Menu")
            .build();
  }

  @Override
  public void init(Player player, InventoryContents inventoryContents) {
    List<String> suffixList = plugin.getConfig().getStringList("suffix.list");
    for (String s : suffixList) {
      String formatted = formatSuffix(s, true);
      Component formattedDeserialized = MessageSender.miniMessage.deserialize(formatted);
      ItemStack item;
      ItemMeta meta;
      if (player.hasPermission("group." + s)) {
        item = new ItemStack(Material.EMERALD_BLOCK, 1);
        meta = item.getItemMeta();
        assert meta != null;
        meta.displayName(formattedDeserialized);
        meta.lore(List.of(Component.text("Equipped!").color(NamedTextColor.AQUA)));
        item.setItemMeta(meta);
        inventoryContents.add(ClickableItem.of(item, e -> {
          e.setCancelled(true);
          PermissionManager.groupUpdate(plugin, player.getUniqueId(), s, false);
          MessageSender.sendMessage(player, "Suffix unequipped.");
          player.closeInventory();
        }));

      } else if (player.hasPermission("ashutils.suffix." + s)) {
        item = new ItemStack(Material.DEEPSLATE, 1);
        meta = item.getItemMeta();
        assert meta != null;
        meta.displayName(formattedDeserialized);
        meta.lore(List.of(Component.text("Equip").color(NamedTextColor.GREEN)));
        item.setItemMeta(meta);
        inventoryContents.add(ClickableItem.of(item, e -> {
          e.setCancelled(true);
          for (String s1 : suffixList) {
            if (player.hasPermission("group." + s1)) {
              PermissionManager.groupUpdate(plugin, player.getUniqueId(), s1, false);
            }
          }
          PermissionManager.groupUpdate(plugin, player.getUniqueId(), s, true);
          MessageSender.sendMessage(player, "Suffix changed to " + formatSuffix(s, true) + ".");
          player.closeInventory();
        }));
      }
    }
    ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
    SkullMeta playerHeadMeta = (SkullMeta) playerHead.getItemMeta();
    assert playerHeadMeta != null;
    playerHeadMeta.setOwningPlayer(player);
    playerHeadMeta.displayName(Component.text("Want more suffixes?").color(NamedTextColor.GOLD));
    playerHeadMeta.lore(List.of(Component.text("Click here to find out more!").color(NamedTextColor.WHITE)));
    playerHead.setItemMeta(playerHeadMeta);
    inventoryContents.add(ClickableItem.of(playerHead, e -> {
      e.setCancelled(true);
      MessageSender.sendMessage(player, "<aqua><click:open_url:'" + plugin.getConfig()
              .getString("suffix.url") + "'>Click here</click></aqua> to learn more about suffixes!");
      player.closeInventory();
      }));

  }

  @Override
  public void update(Player player, InventoryContents inventoryContents) {
  }

  public static String formatSuffix(String s, boolean miniMessage) {
    String formatted = "the " + WordUtils.capitalizeFully(s.replace('_', ' '));
    boolean capitalise = false;
    char[] formattedArray = formatted.toCharArray();
    for (int i = 0; i < formattedArray.length; i++) {
      if (formattedArray[i] == '-') {
        capitalise = true;
        continue;
      } else if (capitalise) {
        formattedArray[i] = Character.toUpperCase(formattedArray[i]);
        break;
      }
      capitalise = false;
    }
    if (miniMessage) {
      return "<gray><italic>" + new String(formattedArray) + "</italic></gray>";
    }
    return "&7&os" + new String(formattedArray);
  }
}
