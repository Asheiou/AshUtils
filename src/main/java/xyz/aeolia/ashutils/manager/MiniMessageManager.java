package xyz.aeolia.ashutils.manager;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class MiniMessageManager {
  private static MiniMessage miniMessage;
  private static BukkitAudiences adventure;

  public static @NotNull BukkitAudiences adventure() {
    if (adventure == null) {
      throw new IllegalStateException("Adventure has not been initialized");
    }
    return adventure;
  }

  public static void init(JavaPlugin plugin) {
    miniMessage = MiniMessage.builder()
            .tags(TagResolver.builder()
                    .resolver(TagResolver.standard())
                    .build()
            )
            .build();
    adventure = BukkitAudiences.create(plugin);
  }

  public static MiniMessage getMiniMessage() {
    return miniMessage;
  }
}
