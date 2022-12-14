package me.lewd.stencilutilities.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import me.lewd.stencilutilities.Main;
import me.lewd.stencilutilities.utils.ChatUtils;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.group.GroupManager;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeEqualityPredicate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SetRank implements CommandExecutor {
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

        if (args.length <= 0) {
            String tooFewArgs = chatUtils.translate(lang.getString("too-few-args"));
            player.sendMessage(chatUtils.getPrefix() + tooFewArgs);
            return true;
        }

        switch(args[0].toLowerCase()) {
            case "random" -> {
                if(!hasPermission(player, "stencil.setrank.random")) {
                    String noPermission = chatUtils.translate(lang.getString("no-permission"));
                    player.sendMessage(chatUtils.getPrefix() + noPermission);
                    return true;
                }

                List<String> randomRankNames = config.getStringList("random-rank-names");
                String randomRank = randomRankNames.get(new Random().nextInt(randomRankNames.size()));

                while (PlaceholderAPI.setPlaceholders(player, "%stencil_rank%").equals(randomRank)) {
                    randomRank = randomRankNames.get(new Random().nextInt(randomRankNames.size()));
                }

                String defaultColor = config.getString("ranks.default.default-color");
                setRank(player, defaultColor + randomRank);
                String setRank = chatUtils.translate(lang.getString("set-rank"));
                player.sendMessage(chatUtils.getPrefix() + PlaceholderAPI.setPlaceholders(player, setRank));
                return true;
            }

            default -> {
                if(!hasPermission(player, "stencil.setrank")) {
                    String noPermission = chatUtils.translate(lang.getString("no-permission"));
                    player.sendMessage(chatUtils.getPrefix() + noPermission);
                    return true;
                }

                String rawRankName = args[0];
                String plainRankName = args[0].replaceAll("(&[a-zA-Z\\d])", "");

                List<String> bannedWords = config.getStringList("blacklisted-rank-names");
                for (String word : bannedWords) {
                    if (plainRankName.contains(word)) return true;
                }

                // lowest permission comes first (e.g.: ranks.default.permission)
                Set<String> configKeys = config.getKeys(true);
                ArrayList<String> rankPermission = new ArrayList<String>();
                rankPermission.add("ranks.default.permission");
                for (String key : configKeys) {
                    if (!Objects.equals(key, rankPermission.get(0)) && key.startsWith("ranks.") && key.endsWith(".permission")) {
                        rankPermission.add(key);
                    }
                }

                for (int i = rankPermission.size() - 1; i >= 0; i--) {
                    if (player.isOp()) {
                        String rankInConfig = rankPermission.get(rankPermission.size() - 1).replace(".permission", "");

                        setRank(player, rawRankName, rankInConfig);
                        String setRank = chatUtils.translate(lang.getString("set-rank"));
                        player.sendMessage(chatUtils.getPrefix() + PlaceholderAPI.setPlaceholders(player, setRank));
                        return true;
                    }

                    if (hasPermission(player, config.getString(rankPermission.get(i)))) {
                        String rankInConfig = rankPermission.get(i).replace(".permission", "");

                        setRank(player, rawRankName, rankInConfig);
                        String setRank = chatUtils.translate(lang.getString("set-rank"));
                        player.sendMessage(chatUtils.getPrefix() + PlaceholderAPI.setPlaceholders(player, setRank));
                        return true;
                    }
                }
                return true;
            }
        }
    }

    private void setRank(Player player, String rank) {
        ranks.set(player.getName(), rank);
        Main.instance.reloadConfigs();
    }

    private void setRank(Player player, String rawName, String configLocation) {
        if (configLocation == null) configLocation = "ranks.default";
        String plainName = rawName.replaceAll("(&[a-zA-Z\\d])", "");

        boolean allowColor = config.getBoolean(configLocation + ".allow-color-codes");
        boolean allowFormat = config.getBoolean(configLocation + ".allow-format-codes");
        boolean includeCodesInCharLimit = config.getBoolean(configLocation + ".include-codes-in-char-limit");
        String defaultColor = config.getString(configLocation + ".default-color");
        int charLimit = config.getInt(configLocation + ".char-limit");
        String permission = config.getString(configLocation + ".permission");

        String rank = chatUtils.translateRank(rawName, defaultColor, allowColor, allowFormat);

        boolean tooManyChars = (includeCodesInCharLimit && rawName.length() > charLimit) || (!includeCodesInCharLimit && plainName.length() > charLimit);
        if (tooManyChars) {
            String nameTooLong = chatUtils.translate(lang.getString("rank-name-too-long"));
            player.sendMessage(chatUtils.getPrefix() + nameTooLong);
            return;
        }

        setPermission(player, permission);
        ranks.set(player.getName(), rank);
        Main.instance.reloadConfigs();
    }

    private void setPermission(Player player, String permission) {
        UserManager userManager = luckPerms.getUserManager();

        User user = userManager.getUser(player.getUniqueId());
        DataMutateResult result = user.data().add(Node.builder(permission).build());

        userManager.saveUser(user);
    }

    private boolean hasPermission(Player player, String permission) {
        UserManager userManager = luckPerms.getUserManager();
        GroupManager groupManager = luckPerms.getGroupManager();

        User user = userManager.getUser(player.getUniqueId());

        boolean playerHasPermission = user.data().contains(Node.builder(permission).build(), NodeEqualityPredicate.EXACT).asBoolean();
        boolean groupHasPermission = groupManager.getGroup(user.getPrimaryGroup()).data().contains(Node.builder(permission).build(), NodeEqualityPredicate.EXACT).asBoolean();

        return playerHasPermission || groupHasPermission || player.isOp();
    }
}