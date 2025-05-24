package uk.asheiou.ashutils;

import java.io.File;
import java.time.Duration;
import java.time.Instant;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import uk.asheiou.ashutils.command.*;
import uk.asheiou.ashutils.listener.BukkitEventListener;
import uk.asheiou.ashutils.listener.EssEventListener;
import uk.asheiou.ashutils.restartonempty.ROEToggle;

public final class AshUtils extends JavaPlugin {

  @Override
  public void onEnable() {
    getLogger().info("Starting load.");
    Instant startTime = Instant.now();
    PluginManager pm = getServer().getPluginManager();
    // // // // // // // // Events // // // // // // // //
    pm.registerEvents(new BukkitEventListener(this), this);
    if (pm.getPlugin("DiscordSRV") != null && pm.getPlugin("Essentials") != null) {
      pm.registerEvents(new EssEventListener(this), this);
    } else { 
      getLogger().severe("Dependencies not found! Disabling.");
      pm.disablePlugin(this);  
    } 
    // // // // // // // // Vault // // // // // // // //
    if (!EconManager.setupEconomy(this)) {
      getLogger().severe("Vault dependency not found! Disabling.");
      pm.disablePlugin(this);
    }
    // // // // // // // // Config // // // // // // // //
    if (!new File(getDataFolder(), "config.yml").exists()) {
      saveDefaultConfig();
      getLogger().info("Config file not found! Creating one.");
    }
    getConfig().options().copyDefaults(true);
    saveConfig();
    // // // // // // // // Commands // // // // // // // //
    this.getCommand("headsell").setExecutor(new HeadSellCommandExecutor(this));
    this.getCommand("ashutils").setExecutor(new AshUtilsTabExecutor());
    this.getCommand("ashutils").setTabCompleter(new AshUtilsTabExecutor());
    getLogger().info("Commands and events registered.");
    // // // // // // // // RestartOnEmpty // // // // // // // //
    ROEToggle.setStatus(false);

    Instant endTime = Instant.now();
    getLogger().info("\u001B[32mLoad complete in " + Duration.between(startTime, endTime).toMillis() + "ms.\u001B[0m");
  }
}
