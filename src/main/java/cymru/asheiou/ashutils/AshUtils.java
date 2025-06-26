package cymru.asheiou.ashutils;

import cymru.asheiou.ashutils.command.NotEnabledCommandExecutor;
import cymru.asheiou.ashutils.command.admin.FakeTabExecutor;
import cymru.asheiou.ashutils.command.admin.VanishOnLoginTabExecutor;
import cymru.asheiou.ashutils.command.admin.ashutils.AshUtilsTabExecutor;
import cymru.asheiou.ashutils.command.user.*;
import cymru.asheiou.ashutils.listener.BukkitEventListener;
import cymru.asheiou.ashutils.listener.EssEventListener;
import cymru.asheiou.ashutils.listener.MineListener;
import cymru.asheiou.ashutils.manager.EconManager;
import cymru.asheiou.ashutils.manager.LuckPermsManager;
import cymru.asheiou.ashutils.manager.StatusManager;
import cymru.asheiou.ashutils.manager.UserMapManager;
import cymru.asheiou.ashutils.user.UserHelper;
import cymru.asheiou.configmanager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
import java.time.Instant;

public class AshUtils extends JavaPlugin {

  @Override
  public void onEnable() {
    getLogger().info("Starting load.");
    Instant startTime = Instant.now();
    PluginManager pm = getServer().getPluginManager();
    // // // // // // // // User // // // // // // // //
    UserHelper.init(this);
    // // // // // // // // Config // // // // // // // //
    new ConfigManager(this, true).loadConfig();
    getConfig().options().copyDefaults(true);
    saveConfig();
    // // // // // // // // Events // // // // // // // //
    MineListener mineListener = new MineListener(this);
    mineListener.init();
    Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
      public void run() {
        mineListener.tick();
      }
    }, 1L, 1L);
    pm.registerEvents(new BukkitEventListener(this), this);
    // // // // // // // // Essentials // // // // // // // //
    if (pm.getPlugin("Essentials") != null) {
      pm.registerEvents(new EssEventListener(this), this);
      this.getCommand("code").setExecutor(new CodeCommandExecutor(this));
    }
    // // // // // // // // LuckPerms // // // // // // // //
    if (pm.getPlugin("LuckPerms") != null) {
      LuckPermsManager.luckPermsSetup();
      UserMapManager.loadUserMap();
      this.getCommand("vanishonlogin").setExecutor(new VanishOnLoginTabExecutor(this));
      this.getCommand("vanishonlogin").setTabCompleter(new VanishOnLoginTabExecutor(this));
      Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
        public void run() {
          UserMapManager.saveUserMap();
        }
      }, 6000L, 6000L);
      // // // // // // // // SmartInvs // // // // // // // //
      if (pm.getPlugin("SmartInvs") != null) {
        this.getCommand("suffix").setExecutor(new SuffixCommandExecutor(this));
      } else {
        this.getCommand("suffix").setExecutor(new NotEnabledCommandExecutor());
        Bukkit.getLogger().info("SmartInvs not found - not enabling /suffix.");
      }

    } else {
      Bukkit.getLogger().info("LuckPerms not found - not enabling permission features.");
      this.getCommand("vanishonlogin").setExecutor(new NotEnabledCommandExecutor());
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
    this.getCommand("report").setExecutor(new ReportCommandExecutor(this));
    getLogger().info("Commands and events registered.");
    // // // // // // // // Toggles // // // // // // // //
    StatusManager.setStatus("restartonempty", false);
    StatusManager.setStatus("lockchat", false);

    Instant endTime = Instant.now();
    getLogger().info("\u001B[32mLoad complete in " + Duration.between(startTime, endTime).toMillis() + "ms.\u001B[0m");
  }

  public void onDisable() {
    UserMapManager.saveUserMap();
  }
}
