package xyz.aeolia.ashutils.manager;

import com.earth2me.essentials.libs.kyori.adventure.platform.bukkit.BukkitAudiences;
import com.earth2me.essentials.libs.kyori.adventure.text.minimessage.MiniMessage;
import com.earth2me.essentials.libs.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import com.earth2me.essentials.libs.kyori.adventure.text.minimessage.tag.standard.StandardTags;
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
                    .resolver(StandardTags.color())
                    .build()
            )
            .build();
    adventure = BukkitAudiences.create(plugin);
  }

  public static MiniMessage getMiniMessage() {
    return miniMessage;
  }
}
