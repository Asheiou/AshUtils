package uk.asheiou.restartonempty;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.scheduler.BukkitRunnable;

public class ROEQuitTask extends BukkitRunnable {
  
  @Override
  public void run() {
    Bukkit.getLogger().info("Player left. Checking if the server should restart...");
    boolean isEmpty = Bukkit.getOnlinePlayers().size() == 0;
    
    if (isEmpty && ROEToggle.getStatus()) {
      Server server = Bukkit.getServer();
      Bukkit.getLogger().info("Restarting as the server is empty.");
      server.dispatchCommand(server.getConsoleSender(), "restart");
    } else {
      Bukkit.getLogger().info("No restart because " + (isEmpty ? "ROE is disabled." : "the server is not empty."));
    }
    
    this.cancel();
  }
}
