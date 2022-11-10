package me.lewd.stencilutilities.listeners;

import me.lewd.stencilutilities.Main;
import me.lewd.stencilutilities.utils.ChatUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.Set;

public class ChatListener implements Listener {
    private final ChatUtils chatUtils = new ChatUtils();
    private final FileConfiguration config = Main.instance.getConfig();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (!config.getBoolean("features.text-replace")) return;

        Player player = e.getPlayer();
        String message = e.getMessage();

        Set<String> configKeys = config.getKeys(true);
        ArrayList<String> keys = new ArrayList<String>();

        configKeys.forEach(key -> {
            if (!key.startsWith("text-replace") || key.contentEquals("text-replace")) return;
            keys.add(key);
        });

        for (String key : keys) {
            String replace = key.replace("text-replace.", "");
            String replacement = chatUtils.translate(config.getString(key));

            if (replacement == null) {
                System.out.println("Replacement text for: \"" + replace + "\" is NULL");
                return;
            }

            message = message.replace(replace, replacement);
        }

        e.setMessage(message);
    }
}