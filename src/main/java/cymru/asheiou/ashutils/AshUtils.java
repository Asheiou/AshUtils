package cymru.asheiou.ashutils;

import java.time.Duration;
import java.time.Instant;

import cymru.asheiou.ashutils.command.*;
import cymru.asheiou.ashutils.command.ashutils.AshUtilsTabExecutor;
import cymru.asheiou.ashutils.manager.EconManager;
import cymru.asheiou.ashutils.manager.LuckPermsManager;
import cymru.asheiou.ashutils.manager.StatusManager;
import cymru.asheiou.ashutils.manager.UserMapManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import cymru.asheiou.ashutils.listener.BukkitEventListener;
import cymru.asheiou.ashutils.listener.EssEventListener;
import cymru.asheiou.configmanager.ConfigManager;

public final class AshUtils extends JavaPlugin {

  @Override
  public void onEnable() {

    getLogger().info("Starting load.");
    Instant startTime = Instant.now();
    PluginManager pm = getServer().getPluginManager();
    // // // // // // // // Config // // // // // // // //
    new ConfigManager(this, true).loadConfig();
    getConfig().options().copyDefaults(true);
    saveConfig();
    // // // // // // // // Events // // // // // // // //
    pm.registerEvents(new BukkitEventListener(this), this);
    // // // // // // // // Dependencies // // // // // // // //
    if (pm.getPlugin("Essentials") != null) {
      if(pm.getPlugin("DiscordSRV") != null) {
        pm.registerEvents(new EssEventListener(this), this);
      } else {
        getLogger().warning("Dependencies for EssEventListener not found - not enabling it.");
      } if (pm.getPlugin("LuckPerms") != null) {
        LuckPermsManager.luckPermsSetup();
        UserMapManager.loadUserMap();
        this.getCommand("vanishonlogin").setExecutor(new VanishOnLoginTabExecutor(this));
        this.getCommand("vanishonlogin").setTabCompleter(new VanishOnLoginTabExecutor(this));
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
          public void run() {
            UserMapManager.saveUserMap();
          }
        }, 6000L, 6000L);
      } else {
        Bukkit.getLogger().info("LuckPerms not found - not enabling VanishOnLogin.");
        this.getCommand("vanishonlogin").setExecutor(new NotEnabledCommandExecutor());
      }
    } 
    // // // // // // // // Vault // // // // // // // //
    if (EconManager.setupEconomy(this)) {
      // TODO: Make this more elegant with a Map or something
      this.getCommand("headsell").setExecutor(new HeadSellCommandExecutor(this));
      this.getCommand("xpbuy").setExecutor(new XpCommandExecutor(this));
      this.getCommand("xpsell").setExecutor(new XpCommandExecutor(this));
    } else {
      getLogger().warning("Vault or economy plugin not found - not enabling economy commands.");
      this.getCommand("headsell").setExecutor(new NotEnabledCommandExecutor());
      this.getCommand("xpbuy").setExecutor(new NotEnabledCommandExecutor());
      this.getCommand("xpsell").setExecutor(new NotEnabledCommandExecutor());
    }
    // // // // // // // // Commands // // // // // // // //
    this.getCommand("ashutils").setExecutor(new AshUtilsTabExecutor(this));
    this.getCommand("ashutils").setTabCompleter(new AshUtilsTabExecutor(this));
    this.getCommand("fake").setExecutor(new FakeTabExecutor(this));
    this.getCommand("fake").setTabCompleter(new FakeTabExecutor(this));
    getLogger().info("Commands and events registered.");
    // // // // // // // // RestartOnEmpty // // // // // // // //
    StatusManager.setStatus("restartonempty", false);
    StatusManager.setStatus("lockchat", false);

    Instant endTime = Instant.now();
    getLogger().info("\u001B[32mLoad complete in " + Duration.between(startTime, endTime).toMillis() + "ms.\u001B[0m");
  }

  public void onDisable() {
    UserMapManager.saveUserMap();
  }
}
