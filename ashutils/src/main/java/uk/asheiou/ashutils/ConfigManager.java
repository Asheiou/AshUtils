package uk.asheiou.ashutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;

import com.earth2me.essentials.config.holders.UserConfigHolder;

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
    try {
      userInput = new FileInputStream(new File(plugin.getDataFolder() + File.separator + "config.yml"));
    } catch (FileNotFoundException e) {
      plugin.saveDefaultConfig();
      plugin.getLogger().info("Config file not found! Creating one.");
      return -1;
    }

    InputStream defaultInput;
    defaultInput = getClass().getClassLoader().getResourceAsStream("config.yml");
    Map<String, Object> userConfig = (Map<String, Object>) yaml.load(userInput);
    Map<String, Object> defaultConfig = (Map<String, Object>) yaml.load(defaultInput);

    if (userConfig == null) {
      plugin.saveDefaultConfig();
      plugin.getLogger().info("Config file empty or unreadable! Creating a new one.");
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
    return amountAdded;
  }
}