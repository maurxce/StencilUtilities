package me.lewd.stencilutilities.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.lewd.stencilutilities.Main;
import me.lewd.stencilutilities.utils.ChatUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;

public class Rank extends PlaceholderExpansion {
    private ChatUtils chatUtils = new ChatUtils();
    private FileConfiguration ranks = Main.instance.getRanksConfig();

    @Override
    public @NotNull String getIdentifier() {
        return "stencil";
    }

    @Override
    public @NotNull String getAuthor() {
        return Main.instance.getAuthor();
    }

    @Override
    public @NotNull String getVersion() {
        return Main.instance.getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (params.equalsIgnoreCase("rank")) {
            Set<String> playerList = ranks.getKeys(false);

            String response = null;
            for (String p : playerList) {
                if (Objects.equals(p, player.getName())) response = ranks.getString(p);
            }

            if (response != null) return chatUtils.translate(response) + " ";
        }

        if (params.equalsIgnoreCase("player")) return player.getName();

        return null;
    }
}
