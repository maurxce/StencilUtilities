package me.lewd.stencilutilities.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import me.lewd.stencilutilities.Main;
import me.lewd.stencilutilities.utils.ChatUtils;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DelRank implements CommandExecutor {
    private ChatUtils chatUtils = new ChatUtils();
    private FileConfiguration config = Main.instance.getConfig();
    private FileConfiguration lang = Main.instance.getLangConfig();
    private FileConfiguration ranks = Main.instance.getRanksConfig();
    private LuckPerms luckPerms = Main.instance.getLuckPerms();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!config.getBoolean("features.ranks")) return true;
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (!player.hasPermission("stencil.delrank")) {
            String noPermission = chatUtils.translate(lang.getString("no-permission"));
            player.sendMessage(chatUtils.getPrefix() + noPermission);
            return true;
        }

        // check args (0 => own rank, 1 => other)
        if (args.length <= 0 || player == Bukkit.getPlayer(args[0])) {
            ranks.set(player.getName(), null);

            String deletedRank = chatUtils.translate(lang.getString("del-rank"));
            player.sendMessage(chatUtils.getPrefix() + PlaceholderAPI.setPlaceholders(player, deletedRank));
            return true;
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                String notFound = chatUtils.translate(lang.getString("player-not-found"));
                player.sendMessage(chatUtils.getPrefix() + notFound);
                return true;
            }

            ranks.set(target.getName(), null);
            String deletedRank = chatUtils.translate(lang.getString("del-rank"));
            player.sendMessage(chatUtils.getPrefix() + PlaceholderAPI.setPlaceholders(target, deletedRank));
            return true;
        }
    }
}
