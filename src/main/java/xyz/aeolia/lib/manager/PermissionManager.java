package xyz.aeolia.lib.manager;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PermissionManager {

  public static LuckPerms api;

  public static boolean groupUpdate(JavaPlugin plugin, UUID uuid, String groupname, boolean status) {
    UserManager userManager = api.getUserManager();
    CompletableFuture<User> userFuture = userManager.loadUser(uuid);
    userFuture.thenAcceptAsync(user -> {
      if (status) {
        user.data().add(Node.builder("group." + groupname).build());
      } else {
        Group group = api.getGroupManager().getGroup(groupname);
        if (group == null) {
          plugin.getLogger().warning("Group " + groupname + " not found when trying to remove permission");
          return;
        }
        user.data().remove(InheritanceNode.builder(group).build());
      }
      userManager.saveUser(user);
    });
    return true;
  }

  public static boolean permissionUpdate(UUID uuid, String permission, boolean status) {
    UserManager userManager = api.getUserManager();
    CompletableFuture<User> userFuture = userManager.loadUser(uuid);
    userFuture.thenAcceptAsync(user -> {
      if (status) {
        user.data().add(Node.builder(permission).build());
      } else {
        user.data().remove(Node.builder(permission).build());
      }
      userManager.saveUser(user);
    });
    return true;
  }

  public static boolean luckPermsSetup() {
    RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
    if (provider != null) {
      api = provider.getProvider();
      return true;
    }
    return false;
  }
}
