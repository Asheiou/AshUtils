package xyz.aeolia.ashutils.manager;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.aeolia.ashutils.object.User;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class UserManager {
  private static final Gson gson = new Gson();
  private static JavaPlugin plugin;
  private static File folder;
  private static final HashMap<UUID, User> users = new HashMap<>();


  public static void init(JavaPlugin plugin) {
    UserManager.plugin = plugin;
    folder = new File(plugin.getDataFolder() + "/users/");
    if (!folder.exists()) {
      if (!folder.mkdir()) {
        Bukkit.getLogger().severe("Failed to create folder " + folder.getAbsolutePath());
        plugin.getServer().getPluginManager().disablePlugin(plugin);
      }
    }
  }

  public static User getUser(UUID uuid) {
    User user;
    user = users.get(uuid);
    if (user == null) {
      File file = new File(folder, uuid.toString() + ".json");
      if (!file.exists()) {
        user = getDefaultUser();
        user.setUuid(uuid);
        putUser(user);
        return user;
      }
      try {
        user = gson.fromJson(new FileReader(file), User.class);
        putUser(user);
        return user;
      } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
      }
    }
    return user;
  }

  public static User getUser(Player player) {
    return getUser(player.getUniqueId());
  }

  public static void putUser(User user) {
    users.put(user.getUuid(), user);
  }

  public static void removeUser(UUID uuid) {
    saveUser(getUser(uuid)); // save user to prevent data loss from prune
    users.remove(uuid);
  }

  public static void saveUser(User user) {
    File file = new File(folder, user.getUuid().toString() + ".json");
    try {
      FileWriter fileWriter = new FileWriter(file);
      fileWriter.write(gson.toJson(user));
      fileWriter.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void saveUsers() {
    if (users.isEmpty()) {
      return;
    }
    CompletableFuture.supplyAsync(() -> {
      for (User user : users.values()) {
        saveUser(user);
      }
      plugin.getLogger().info("Saved " + users.size() + " users!");
      return null;
    });
  }

  public static User getDefaultUser() {
    InputStream defaultFile = UserManager.class.getClassLoader().getResourceAsStream("default.json");

    if (defaultFile == null) {
      Bukkit.getLogger().severe("Missing resource in jar! Check your build.");
      plugin.getServer().getPluginManager().disablePlugin(plugin);
    }
    assert defaultFile != null;
    return gson.fromJson(new InputStreamReader(defaultFile), User.class);
  }
}
