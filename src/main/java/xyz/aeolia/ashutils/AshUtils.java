package xyz.aeolia.ashutils;

import cymru.asheiou.configmanager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.aeolia.ashutils.command.NotEnabledCommandExecutor;
import xyz.aeolia.ashutils.command.admin.FakeTabExecutor;
import xyz.aeolia.ashutils.command.admin.ModCommandExecutor;
import xyz.aeolia.ashutils.command.admin.VanishOnLoginTabExecutor;
import xyz.aeolia.ashutils.command.admin.ashutils.AshUtilsTabExecutor;
import xyz.aeolia.ashutils.command.user.*;
import xyz.aeolia.ashutils.listener.*;
import xyz.aeolia.ashutils.manager.*;
import xyz.aeolia.ashutils.user.UserHelper;

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
    UserMapManager.loadUserMap();
    Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this::saveAll, 6000L, 6000L);
    // // // // // // // // Config // // // // // // // //
    new ConfigManager(this, true).loadConfig();
    getConfig().options().copyDefaults(true);
    saveConfig();
    // // // // // // // // Events // // // // // // // //
    MineListener mineListener = new MineListener(this);
    mineListener.init();
    Bukkit.getScheduler().scheduleSyncRepeatingTask(this, mineListener::tick, 1L, 1L);
    pm.registerEvents(new BukkitEventListener(this), this);
    // // // // // // // // Essentials // // // // // // // //
    if (pm.getPlugin("Essentials") == null) {
      pm.disablePlugin(this);
    }
    MiniMessageManager.init();
    pm.registerEvents(new EssEventListener(this), this);
    this.getCommand("code").setExecutor(new CodeCommandExecutor(this));
    // // // // // // // // LuckPerms // // // // // // // //
    if (pm.getPlugin("LuckPerms") != null) {
      LuckPermsManager.luckPermsSetup();
      this.getCommand("vanishonlogin").setExecutor(new VanishOnLoginTabExecutor(this));
      this.getCommand("vanishonlogin").setTabCompleter(new VanishOnLoginTabExecutor(this));

      // // // // // // // // SmartInvs // // // // // // // //
      if (pm.getPlugin("SmartInvs") != null) {
        this.getCommand("suffix").setExecutor(new SuffixCommandExecutor(this));
        pm.registerEvents(new SuffixListener(this), this);
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
      pm.registerEvents(new VaultListener(this), this);
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
    this.getCommand("mod").setExecutor(new ModCommandExecutor());
    this.getCommand("report").setExecutor(new ReportCommandExecutor(this));
    getLogger().info("Commands and events registered.");
    // // // // // // // // Toggles // // // // // // // //
    StatusManager.setStatus("restartonempty", false);
    StatusManager.setStatus("lockchat", false);

    Instant endTime = Instant.now();
    getLogger().info("\u001B[32mLoad complete in " + Duration.between(startTime, endTime).toMillis() + "ms.\u001B[0m");
  }

  public void onDisable() { saveAll(); }

  public void saveAll() {
    UserMapManager.saveUserMap();
    UserHelper.saveUsers();
  }
}