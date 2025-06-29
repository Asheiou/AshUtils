package xyz.aeolia.ashutils.manager;

import com.earth2me.essentials.libs.kyori.adventure.text.minimessage.MiniMessage;
import com.earth2me.essentials.libs.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import com.earth2me.essentials.libs.kyori.adventure.text.minimessage.tag.standard.StandardTags;

public class MiniMessageManager {
  private static MiniMessage miniMessage;

  public static void init() {
    miniMessage = MiniMessage.builder()
            .tags(TagResolver.builder()
                    .resolver(StandardTags.color())
                    .build()
            )
            .build();
  }

  public static MiniMessage getMiniMessage() {
    return miniMessage;
  }
}
