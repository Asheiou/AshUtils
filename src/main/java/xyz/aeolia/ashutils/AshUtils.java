package xyz.aeolia.ashutils;

import cymru.asheiou.configmanager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabExecutor;
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
import xyz.aeolia.ashutils.sender.MessageSender;
import xyz.aeolia.ashutils.user.UserHelper;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AshUtils extends JavaPlugin {

  @Override
  public void onEnable() {
    getLogger().info("Starting load.");
    Instant startTime = Instant.now();
    PluginManager pm = getServer().getPluginManager();
    // // // // // // // // Sender // // // // // // // //
    MessageSender.init(this);
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
    // // // // // // // // Commands // // // // // // // //
    AshUtils plugin = this;
    Map<String, CommandExecutor> commands = new HashMap<>(){
      {
        put("ashutils", new AshUtilsTabExecutor(plugin));
        put("code", new CodeCommandExecutor(plugin));
        put("mod", new ModCommandExecutor());
        put("report", new ReportCommandExecutor(plugin));
        put("fake", new FakeTabExecutor(plugin));
      }
    };
    // // // // // // // // Essentials // // // // // // // //
    if (pm.getPlugin("Essentials") == null) {
      pm.disablePlugin(this);
    }
    pm.registerEvents(new EssEventListener(this), this);
    // // // // // // // // LuckPerms // // // // // // // //
    if (pm.getPlugin("LuckPerms") != null) {
      LuckPermsManager.luckPermsSetup();
      commands.put("vanishonlogin", new VanishOnLoginTabExecutor(this));
      // // // // // // // // SmartInvs // // // // // // // //
      if (pm.getPlugin("SmartInvs") != null) {
        commands.put("suffix", new SuffixCommandExecutor(this));
        pm.registerEvents(new SuffixListener(this), this);
      } else {
        commands.put("suffix", new NotEnabledCommandExecutor());
        Bukkit.getLogger().info("SmartInvs not found - not enabling /suffix.");
      }

    } else {
      Bukkit.getLogger().info("LuckPerms not found - not enabling permission features.");
      commands.put("vanishonlogin", new NotEnabledCommandExecutor());
    }
    // // // // // // // // Vault // // // // // // // //
    if (EconManager.setupEconomy(this)) {
      pm.registerEvents(new VaultListener(this), this);
      commands.put("headsell", new HeadSellCommandExecutor(this));
      commands.put("xpbuy", new XpCommandExecutor(this));
      commands.put("xpsell", new XpCommandExecutor(this));
    } else {
      getLogger().warning("Vault or economy plugin not found - not enabling economy commands.");
      commands.put("headsell", new NotEnabledCommandExecutor());
      commands.put("xpbuy", new NotEnabledCommandExecutor());
      commands.put("xpsell", new NotEnabledCommandExecutor());
    }
    // // // // // // // // Commands // // // // // // // //
    for (Map.Entry<String, CommandExecutor> entry : commands.entrySet()) {
      setExecutor(entry.getKey(), entry.getValue());
    }
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

  public void setExecutor(String command, CommandExecutor executor) {
    try {
      Objects.requireNonNull(this.getCommand(command)).setExecutor(executor);
      if (executor instanceof TabExecutor tabExecutor) {
        Objects.requireNonNull(this.getCommand(command)).setTabCompleter(tabExecutor);
      }
    } catch (NullPointerException e) {
      this.getLogger().warning(command + " is not registered in the plugin.yml. Check your build.");
    }
  }
}