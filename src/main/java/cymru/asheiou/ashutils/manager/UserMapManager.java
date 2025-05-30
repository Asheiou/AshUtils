package cymru.asheiou.ashutils.manager;

import com.google.gson.Gson;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class UserMapManager {
  private static final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(UserMapManager.class);
  private static final String filePath = plugin.getDataFolder() + "/users.json";

  static Map<String, String> userMap = new HashMap<>();
  static Gson gson = new Gson();

  @SuppressWarnings("unchecked")
  public static void loadUserMap() {
    if (!new File(filePath).exists()) {
      try {
        new File(filePath).createNewFile();
        gson.toJson(new HashMap<String, String>(), new FileWriter(filePath));
        return;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    else {
      CompletableFuture<Void> future = CompletableFuture.supplyAsync(() ->
              {
                try {
                  String jsonContent = Files.readString(Paths.get(filePath));
                  return gson.fromJson(jsonContent, Map.class);
                } catch (Exception e) {
                  throw new RuntimeException(e);
                }
              }
      ).thenAccept(
              result ->
              {
                userMap = (Map<String, String>) result;
                plugin.getLogger().info("users.json loaded with " + userMap.size() + " users!");
              });
    }
  }

  public static UUID getUserFromName(String name) {
    plugin.getLogger().info("Getting user from "+name);
    if(!userMap.containsKey(name)) { return null; }
    return UUID.fromString(userMap.get(name));
  }

  public static void putUserInMap(String name, UUID uuid) {
    userMap.put(name, uuid.toString());
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
