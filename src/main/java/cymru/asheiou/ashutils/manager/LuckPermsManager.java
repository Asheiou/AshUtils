package cymru.asheiou.ashutils.manager;

import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class LuckPermsManager {
  public static LuckPerms api;

  public static boolean luckPermsSetup() {
    RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
    if (provider != null) {
      api = provider.getProvider();
      return true;
    }
    return false;
  }

  public static LuckPerms getApi() {
    return api;
  }
}
