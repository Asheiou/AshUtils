package xyz.aeolia.ashutils.menu;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
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
      String formatted = formatSuffix(s, false);
      ItemStack item;
      ItemMeta meta;
      if (player.hasPermission("group." + s)) {
        item = new ItemStack(Material.EMERALD_BLOCK, 1);
        meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(formatted);
        meta.setLore(List.of(ChatColor.AQUA + "Equipped!"));
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
        meta.setDisplayName(formatted);
        meta.setLore(List.of(ChatColor.GREEN + "Equip"));
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
    playerHeadMeta.setDisplayName(ChatColor.AQUA + "Want more suffixes?");
    playerHeadMeta.setLore(List.of(ChatColor.RESET + "Click here to find out more!"));
    playerHead.setItemMeta(playerHeadMeta);
    inventoryContents.add(ClickableItem.of(playerHead, e -> {
      e.setCancelled(true);
      MessageSender.sendMessage(player, "<aqua><click:open_url:'" + plugin.getConfig()
              .getString("suffix.url") + "'>Click here</click></aqua> to learn more about suffixes!");
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
    return ChatColor.GRAY.toString() + ChatColor.ITALIC + new String(formattedArray);
  }
}
