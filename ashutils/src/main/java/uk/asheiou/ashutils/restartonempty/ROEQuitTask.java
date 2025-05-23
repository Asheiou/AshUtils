package uk.asheiou.ashutils.restartonempty;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import uk.asheiou.ashutils.AshUtils;

public class ROEQuitTask extends BukkitRunnable {
  
  @Override
  public void run() {
    JavaPlugin plugin = JavaPlugin.getProvidingPlugin(AshUtils.class);
    plugin.getLogger().info("Player left. Checking if the server should restart...");
    boolean isEmpty = Bukkit.getOnlinePlayers().size() == 0;
    
    if (isEmpty && ROEToggle.getStatus()) {
      Server server = Bukkit.getServer();
      plugin.getLogger().info("Restarting as the server is empty.");
      server.dispatchCommand(server.getConsoleSender(), "restart");
    } else {
      plugin.getLogger().info("No restart because " + (isEmpty ? "ROE is disabled." : "the server is not empty."));
    }
    
    this.cancel();
  }
}
