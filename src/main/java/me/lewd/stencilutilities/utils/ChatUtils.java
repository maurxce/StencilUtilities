package me.lewd.stencilutilities.utils;

import org.bukkit.ChatColor;

public class ChatUtils {
    public String translate(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
