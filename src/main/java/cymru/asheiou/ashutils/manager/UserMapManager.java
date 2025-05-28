package cymru.asheiou.ashutils.manager;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class UserMapManager {
  private static final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(UserMapManager.class);
  private static final String filePath = plugin.getDataFolder() + "/users.json";

  static Map<String, UUID> userMap = new HashMap<>();
  static Gson gson = new Gson();

  @SuppressWarnings("unchecked")
  public static void loadUserMap() {
    if (!new File(filePath).exists()) {
      try {
        new File(filePath).createNewFile();
        gson.toJson(new HashMap<String, UUID>(), new FileWriter(filePath));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    CompletableFuture<Void> future = CompletableFuture.supplyAsync(() ->
            {
              return gson.fromJson(filePath, Map.class);
            }
    ).thenAccept(
            result ->
                    userMap = (Map<String, UUID>) result);
    plugin.getLogger().info("users.json loaded!");
  }

  public static UUID getUserFromName(String name) {
    return userMap.get(name);
  }

  public static void putUserInMap(String name, UUID uuid) {
    userMap.put(name, uuid);
  }

  public static void saveUserMap() {
    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
      try (FileWriter writer = new FileWriter(filePath)) {
        gson.toJson(userMap, writer);
      } catch (IOException e) {
        throw new CompletionException(e);
      }
    }).thenRun(() ->
            plugin.getLogger().info("Saved users.json with " + userMap.size() + " values!")
    ).exceptionally(ex -> {
      plugin.getLogger().severe("Failed to save users.json! Dumping it to console. Reason: " + ex.getMessage());
      plugin.getLogger().severe("StackTrace: " + Arrays.toString(ex.getStackTrace()));
      plugin.getLogger().severe("users.json: " + gson.toJson(userMap));
      return null;
    });
  }
}
