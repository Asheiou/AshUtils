package uk.asheiou.restartonempty;

import org.bukkit.plugin.java.JavaPlugin;
import java.time.*;

public final class RestartOnEmpty extends JavaPlugin {
  
  @Override
  public void onEnable() {
    getLogger().info("Starting load.");
    Instant startTime = Instant.now();
    
    getServer().getPluginManager().registerEvents(new EventListener(this), this);
    this.getCommand("restartonempty").setExecutor(new ROECommandExecutor());
    
    ROEToggle.setStatus(false);
    
    Instant endTime = Instant.now();
    getLogger().info("\u001B[32mLoad complete in " + Duration.between(startTime, endTime).toMillis() + "ms.\u001B[0m");
  }
}
