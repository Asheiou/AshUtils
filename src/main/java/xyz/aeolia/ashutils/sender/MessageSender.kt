package xyz.aeolia.ashutils.sender

import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.md_5.bungee.chat.ComponentSerializer
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

class MessageSender {
  companion object{
    lateinit var miniMessage : MiniMessage
    lateinit var plugin : JavaPlugin;

    @JvmStatic
    fun init(plugin : JavaPlugin) {
      this.plugin = plugin
      miniMessage = MiniMessage.builder()
        .tags(
          TagResolver.builder()
            .resolver(TagResolver.standard())
            .build()
        )
        .build()
    }

    @JvmStatic
    fun sendMessage(recipient: CommandSender, message: String?, includePrefix: Boolean = true) {
      var toDeserialize: String?;
      if (includePrefix) {
        val prefix = plugin.config.getString("chat-prefix")
        toDeserialize = "$prefix <reset>$message";
      } else toDeserialize = message
      if (toDeserialize.isNullOrEmpty()) return
      val adventureComponent = miniMessage.deserialize(toDeserialize)
      val json = GsonComponentSerializer.gson().serialize(adventureComponent)
      val spigotComponents = ComponentSerializer.parse(json)
      recipient.spigot().sendMessage(*spigotComponents)
    }

    @JvmStatic
    fun sendMessage(recipient: CommandSender, message: String?) {
      // Allow calls from Java
      sendMessage(recipient, message, true)
    }
  }
}