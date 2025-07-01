package xyz.aeolia.ashutils.sender;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.aeolia.ashutils.AshUtils;
import xyz.aeolia.ashutils.manager.MiniMessageManager;

public class MessageSender {
  public static void sendMessage(CommandSender recipient, String message, boolean includePrefix) {
    String prefix = JavaPlugin.getProvidingPlugin(AshUtils.class).getConfig().getString("chat-prefix");
    if (includePrefix) message = prefix + "<reset> " + message;
    Component adventureComponent = MiniMessageManager.getMiniMessage().deserialize(message);
    String json = GsonComponentSerializer.gson().serialize(adventureComponent);
    BaseComponent[] spigotComponents = ComponentSerializer.parse(json);
    recipient.spigot().sendMessage(spigotComponents);
  }

  public static void sendMessage(CommandSender recipient, String message) {
    sendMessage(recipient, message, true);
  }
}