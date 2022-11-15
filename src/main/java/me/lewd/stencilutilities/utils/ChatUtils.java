package me.lewd.stencilutilities.utils;

import me.lewd.stencilutilities.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class ChatUtils {
    private FileConfiguration config = Main.instance.getConfig();

    public String translate(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
    public String translateRank(String rawRank, String defaultColor, boolean color, boolean format) {
        if (!color) rawRank = rawRank.replaceAll("(&[a-fA-F\\d])", "");
        if (!format) rawRank = rawRank.replaceAll("(&[k-o|r])", "");

        return ChatColor.translateAlternateColorCodes('&', defaultColor + rawRank);
    }

    public String getPrefix() {
        if (!config.getBoolean("features.prefix")) return null;
        String prefix = config.getString("prefix");

        if (prefix == null) return null;
        return translate(config.getString("prefix")) + " ";
    }
}
