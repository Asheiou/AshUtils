package cymru.asheiou.ashutils.task;

import com.earth2me.essentials.Essentials;
import cymru.asheiou.ashutils.user.User;
import cymru.asheiou.ashutils.user.UserHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.CompletableFuture;

public class VanishTask extends BukkitRunnable {

  @Override
  public void run() {
    CompletableFuture.supplyAsync(() -> {
      try {
        wait(50L);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      for (Player p : Bukkit.getOnlinePlayers()) {
        if (p.hasPermission("essentials.vanish")) {
          Essentials ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
          User user = UserHelper.getUser(p);
          assert ess != null;
          user.setVanish(ess.getUser(user.getUuid()).isVanished());
        }
      }
    return null; });
  }
}
