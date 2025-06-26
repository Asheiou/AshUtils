package cymru.asheiou.ashutils.user;

import com.google.gson.Gson;
import cymru.asheiou.ashutils.AshUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.UUID;

public class UserHelper {
  private static JavaPlugin plugin;
  private static File folder;
  private static final Gson gson = new Gson();

  public static void init(JavaPlugin plugin) {
    UserHelper.plugin = plugin;
    folder = new File(plugin.getDataFolder()+"/users/");
    if (!folder.exists()) {
      if (!folder.mkdir()) {
        Bukkit.getLogger().severe("Failed to create folder " + folder.getAbsolutePath());
        plugin.getServer().getPluginManager().disablePlugin(plugin);
      }
    }
  }

  public static User getUser(UUID uuid) {
    File file = new File(folder, uuid.toString() + ".json");
    if (!file.exists()) {
      User user = getDefaultUser();
      user.setUuid(uuid);
      return user;
    }
    try {
      return gson.fromJson(new FileReader(file), User.class);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public static User getUser(Player player) {
    return getUser(player.getUniqueId());
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

  public static User getDefaultUser() {
    InputStream defaultFile = AshUtils.class.getResourceAsStream("/default.json");

    if (defaultFile != null) {
      Bukkit.getLogger().severe("Missing resource in jar! Check your build.");
      plugin.getServer().getPluginManager().disablePlugin(plugin);
    }
    assert defaultFile != null;
    return gson.fromJson(defaultFile.toString(), User.class);
  }
}
