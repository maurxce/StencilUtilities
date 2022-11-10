package me.lewd.stencilutilities.listeners;

import me.lewd.stencilutilities.Main;
import net.luckperms.api.LuckPerms;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class JoinListener implements Listener {
    private FileConfiguration config = Main.instance.getConfig();
    private FileConfiguration ranks = Main.instance.getRanksConfig();
    private LuckPerms luckPerms = Main.instance.getLuckPerms();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!config.getBoolean("features.assign-rank-on-join")) return;
        Player player = e.getPlayer();

        Set<String> playerList = ranks.getKeys(false);

        if (!playerList.contains(player.getName())) {
            List<String> randomRankNames = config.getStringList("random-rank-names");
            String name = randomRankNames.get(new Random().nextInt(randomRankNames.size()));

            ranks.set(player.getName(), name);
            Main.instance.reloadConfigs();
        }
    }
}
