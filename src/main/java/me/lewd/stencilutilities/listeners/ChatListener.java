package me.lewd.stencilutilities.listeners;

import me.lewd.stencilutilities.Main;
import me.lewd.stencilutilities.utils.ChatUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Set;

public class ChatListener implements Listener {
    private final FileConfiguration config = Main.instance.getConfig();
    private final ChatUtils chatUtils = new ChatUtils();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        String message = e.getMessage();

        Set<String> textToReplace = config.getKeys(false);

        for (String text : textToReplace) {
            String replacement = chatUtils.translate(config.getString(text));

            if (replacement == null) {
                System.out.println("Replacement text for: \"" + text + "\" is NULL");
                return;
            }

            message = message.replace(text, replacement);
        }

        e.setMessage(message);
    }
}