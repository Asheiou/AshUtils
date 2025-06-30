package xyz.aeolia.ashutils.sender;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.aeolia.ashutils.AshUtils;
import xyz.aeolia.ashutils.manager.MiniMessageManager;

public class MessageSender {
  public static void sendMessage(CommandSender recipient, String message) {
    String prefix = JavaPlugin.getProvidingPlugin(AshUtils.class).getConfig().getString("chat-prefix");
    message = prefix + "<reset> " + message;
    Component deserialized = MiniMessageManager.getMiniMessage().deserialize(message);
    MiniMessageManager.adventure().sender(recipient).sendMessage(MiniMessageManager.getMiniMessage().deserialize("<red>this should be red"));
    MiniMessageManager.adventure().sender(recipient).sendMessage(deserialized);
  }
}