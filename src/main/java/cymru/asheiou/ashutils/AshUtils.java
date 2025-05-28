package cymru.asheiou.ashutils;

import java.time.Duration;
import java.time.Instant;

import cymru.asheiou.ashutils.command.ashutils.AshUtilsTabExecutor;
import cymru.asheiou.ashutils.command.HeadSellCommandExecutor;
import cymru.asheiou.ashutils.command.NotEnabledCommandExecutor;
import cymru.asheiou.ashutils.command.XpCommandExecutor;
import cymru.asheiou.ashutils.manager.EconManager;
import cymru.asheiou.ashutils.manager.StatusManager;
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
    // // // // // // // // Events // // // // // // // //
    pm.registerEvents(new BukkitEventListener(this), this);
    if (pm.getPlugin("DiscordSRV") != null && pm.getPlugin("Essentials") != null) {
      pm.registerEvents(new EssEventListener(this), this);
    } else { 
      getLogger().warning("Dependencies for EssEventListener not found - not enabling it.");
    } 
    // // // // // // // // Vault // // // // // // // //
    if (EconManager.setupEconomy(this)) {
      // TODO: Make this more elegant with a Map or something
      this.getCommand("headsell").setExecutor(new HeadSellCommandExecutor(this));
      this.getCommand("xpbuy").setExecutor(new XpCommandExecutor(this));
      this.getCommand("xpsell").setExecutor(new XpCommandExecutor(this));
    } else {
      getLogger().warning("Vault or economy plugin not found - not enabling /headsell.");
      this.getCommand("headsell").setExecutor(new NotEnabledCommandExecutor());
      this.getCommand("xpbuy").setExecutor(new NotEnabledCommandExecutor());
      this.getCommand("xpsell").setExecutor(new NotEnabledCommandExecutor());
    }
    // // // // // // // // Config // // // // // // // //
    new ConfigManager(this, true).loadConfig();
    getConfig().options().copyDefaults(true);
    saveConfig();
    // // // // // // // // Commands // // // // // // // //
    this.getCommand("ashutils").setExecutor(new AshUtilsTabExecutor(this));
    this.getCommand("ashutils").setTabCompleter(new AshUtilsTabExecutor(this));
    getLogger().info("Commands and events registered.");
    // // // // // // // // RestartOnEmpty // // // // // // // //
    StatusManager.setStatus("restartonempty", false);
    StatusManager.setStatus("lockchat", false);

    Instant endTime = Instant.now();
    getLogger().info("\u001B[32mLoad complete in " + Duration.between(startTime, endTime).toMillis() + "ms.\u001B[0m");
  }
}
