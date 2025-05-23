package uk.asheiou.ashutils;
import org.bukkit.plugin.java.JavaPlugin;

import uk.asheiou.ashutils.restartonempty.ROECommandExecutor;
import uk.asheiou.ashutils.restartonempty.ROEToggle;

import java.time.*;

public final class AshUtils extends JavaPlugin {
  
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
