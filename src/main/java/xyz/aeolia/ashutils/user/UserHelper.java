package xyz.aeolia.ashutils.user;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class UserHelper {
  private static final Gson gson = new Gson();
  private static JavaPlugin plugin;
  private static File folder;
  private static HashMap<UUID, User> users;


  public static void init(JavaPlugin plugin) {
    UserHelper.plugin = plugin;
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
    try {
      user = users.get(uuid);
    } catch (NullPointerException e) {
      user = null;
    }
    if (user == null) {
      File file = new File(folder, uuid.toString() + ".json");
      if (!file.exists()) {
        user = getDefaultUser();
        user.setUuid(uuid);
        users.put(uuid, user);
        return user;
      }
      try {
        user = gson.fromJson(new FileReader(file), User.class);
        users.put(uuid, user);
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
    if (users == null) { return; }
    CompletableFuture.supplyAsync(() -> {
      for (User user : users.values()) {
        saveUser(user);
      }
      return null;
    });
  }

  public static User getDefaultUser() {
    InputStream defaultFile = UserHelper.class.getClassLoader().getResourceAsStream("default.json");

    if (defaultFile == null) {
      Bukkit.getLogger().severe("Missing resource in jar! Check your build.");
      plugin.getServer().getPluginManager().disablePlugin(plugin);
    }
    assert defaultFile != null;
    return gson.fromJson(defaultFile.toString(), User.class);
  }
}
