package uk.asheiou.ashutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.scanner.ScannerException;


public class ConfigManager {
  JavaPlugin plugin;

  public ConfigManager(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  Yaml yaml = new Yaml();

  public static Set<String> checkUniqueKeys(
    Map<String, Object> first, 
    Map<String, Object> second) {
  
    return first.keySet().stream()
        .filter(key -> !second.containsKey(key))
        .collect(Collectors.toSet());
  }
  
  @SuppressWarnings("unchecked")
  public int loadConfig() {
    FileInputStream userInput;
    File userConfigFile;
    try {
      userConfigFile = new File(plugin.getDataFolder(), "config.yml");
      userInput = new FileInputStream(userConfigFile);
    } catch (FileNotFoundException e) {
      plugin.saveDefaultConfig();
      plugin.getLogger().info("Config file not found! Creating one.");
      return -1;
    }

    InputStream defaultInput;
    defaultInput = getClass().getClassLoader().getResourceAsStream("config.yml");
    
    Map<String, Object> userConfig;
    try {
      userConfig = (Map<String, Object>) yaml.load(userInput);
    } catch (ScannerException e) {
      String brokenConfigString = plugin.getDataFolder() + File.separator + "config-broken" + UUID.randomUUID().toString() + ".yml";
      
      try {
        Files.copy(userConfigFile.toPath(), (new File(brokenConfigString).toPath()), StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException e1) {
        e1.printStackTrace();
      }
      userConfigFile.delete();
      plugin.saveDefaultConfig();
      plugin.getLogger().info("Config file unreadable! Creating a new one. Your broken config can be found at " + brokenConfigString);
      return -1;
    }
    
    
    Map<String, Object> defaultConfig;
    try {
      defaultConfig = (Map<String, Object>) yaml.load(defaultInput);
    } catch(ScannerException e) {
      plugin.getLogger().severe("FATAL: Internal config.yml not found! Please check your build. Disabling.");
      plugin.getServer().getPluginManager().disablePlugin(plugin);
      return -1;
    }

    try {
      userInput.close();
      defaultInput.close();
    } catch (IOException e) {
      plugin.getLogger().warning("InputStreams not successfully closed! This may cause a memory leak.");
    }
    
    if (userConfig == null) {
      plugin.saveDefaultConfig();
      plugin.getLogger().info("Config file empty! Creating a new one.");
      plugin.reloadConfig();
      return -1;
    }

    int amountRemoved = 0;
    for (String uniqueKey : checkUniqueKeys(userConfig, defaultConfig)) {
      plugin.getConfig().set(uniqueKey, null); // remove keys that are in the userconfig and not default 
      amountRemoved++;
    }
    
    int amountAdded = 0;
    for (String uniqueKey : checkUniqueKeys(defaultConfig, userConfig)) {
      plugin.getConfig().set(uniqueKey, defaultConfig.get(uniqueKey));
      amountAdded++;
    }

    if (amountAdded > 0 || amountRemoved > 0) {
      plugin.saveConfig();
      plugin.getLogger().info(amountAdded + " config value"
          + (amountAdded == 1 ? " " : "s ") + "added, " + amountRemoved + " removed.");
    }
    plugin.reloadConfig();
    return amountAdded;
  }
}