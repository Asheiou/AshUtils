package cymru.asheiou.ashutils.command;

import cymru.asheiou.ashutils.menu.SuffixMenu;
import cymru.asheiou.ashutils.sender.MessageSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class SuffixCommandExecutor implements CommandExecutor {
  JavaPlugin plugin;
  public SuffixCommandExecutor(JavaPlugin plugin) {
    this.plugin = plugin;
  }
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
    if(!(sender instanceof Player player)) {
      MessageSender.sendMessage(sender, "Only players can execute this command.");
      return true;
    }
    MessageSender.sendMessage(sender, "You are using this command.");
    new SuffixMenu(plugin).INVENTORY.open(player);
    return true;
  }
}
