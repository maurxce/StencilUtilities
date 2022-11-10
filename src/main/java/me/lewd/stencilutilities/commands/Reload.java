package me.lewd.stencilutilities.commands;

import me.lewd.stencilutilities.Main;
import me.lewd.stencilutilities.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Reload implements CommandExecutor {
    private ChatUtils chatUtils = new ChatUtils();
    private FileConfiguration lang = Main.instance.getLangConfig();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (!player.hasPermission("stencil.reload")) {
            String noPermission = chatUtils.translate(lang.getString("no-permission"));
            player.sendMessage(chatUtils.getPrefix() + noPermission);
            return true;
        }

        Bukkit.getLogger().warning("Reloading Config!");
        Main.instance.forceReload();

        String reloaded = chatUtils.translate(lang.getString("config-reloaded"));
        player.sendMessage(chatUtils.getPrefix() + reloaded);
        return true;
    }
}
