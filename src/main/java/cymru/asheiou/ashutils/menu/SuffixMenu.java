package cymru.asheiou.ashutils.menu;

import cymru.asheiou.ashutils.manager.PermissionManager;
import cymru.asheiou.ashutils.sender.MessageSender;
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
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class SuffixMenu implements InventoryProvider  {
  private final JavaPlugin plugin;
  public final SmartInventory INVENTORY;
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
    List<String> suffixlist = plugin.getConfig().getStringList("suffix.list");
    for (String s : suffixlist) {
      String formatted = WordUtils.capitalizeFully(s.replace('_', ' '));
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
      formatted = new String(formattedArray);

      ItemStack item;
      ItemMeta meta;
      if(player.hasPermission("group."+s)) {
        item = new ItemStack(Material.EMERALD_BLOCK, 1);
        meta = item.getItemMeta(); assert meta != null;
        meta.setDisplayName(formatted);
        meta.setLore(List.of(ChatColor.AQUA + "Equipped!"));
        item.setItemMeta(meta);
        inventoryContents.add(ClickableItem.of(item, e -> {
          e.setCancelled(true);
          PermissionManager.groupUpdate(plugin, player.getUniqueId(), s, false);
          MessageSender.sendMessage(player, "Suffix unequipped.");
          player.closeInventory();
        }));

      } else if (player.hasPermission("ashutils.suffix."+s)) {
        item = new ItemStack(Material.DEEPSLATE, 1);
        meta = item.getItemMeta(); assert meta != null;
        meta.setDisplayName(formatted);
        meta.setLore(List.of(ChatColor.GREEN + "Equip"));
        item.setItemMeta(meta);
        String finalFormatted = formatted;
        inventoryContents.add(ClickableItem.of(item, e -> {
          e.setCancelled(true);
          for (String s1 : suffixlist) {
            if (player.hasPermission("group." + s1)) {
              PermissionManager.groupUpdate(plugin, player.getUniqueId(), s1, false);
            }
          }
          PermissionManager.groupUpdate(plugin, player.getUniqueId(), s, true);
          MessageSender.sendMessage(player, "Suffix changed to " + finalFormatted + ".");
          player.closeInventory();
        }));

      } else {
        item = new ItemStack(Material.REDSTONE_BLOCK, 1);
        meta = item.getItemMeta(); assert meta != null;
        meta.setDisplayName(formatted);
        meta.setLore(List.of(ChatColor.RED+ "Not unlocked!"));
        item.setItemMeta(meta);
        inventoryContents.add(ClickableItem.of(item, e -> {
          e.setCancelled(true);
          MessageSender.sendMessage(player, "You don't have this suffix yet!");
          player.closeInventory();
        }));
      }
    }
  }

  @Override
  public void update(Player player, InventoryContents inventoryContents) {

  }
}
