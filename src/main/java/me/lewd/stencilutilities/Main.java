package me.lewd.stencilutilities;

import me.lewd.stencilutilities.commands.DelRank;
import me.lewd.stencilutilities.commands.SetRank;
import me.lewd.stencilutilities.commands.Reload;
import me.lewd.stencilutilities.listeners.ChatListener;
import me.lewd.stencilutilities.listeners.JoinListener;
import me.lewd.stencilutilities.placeholders.Rank;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public final class Main extends JavaPlugin {
    public static Main instance;

    private FileConfiguration config = new YamlConfiguration();
    private FileConfiguration lang = new YamlConfiguration();
    private FileConfiguration ranks = new YamlConfiguration();

    File configFile = new File(getDataFolder(), "config.yml");
    File langFile = new File(getDataFolder(), "lang.yml");
    File ranksFile = new File(getDataFolder(), "ranks.yml");

    private LuckPerms luckPerms;

    @Override
    public void onEnable() {
        instance = this;
        checkDependencies();

        initializeFiles();

        registerEvents();
        registerCommands();
    }

    private void checkDependencies() {
        PluginManager pm = getServer().getPluginManager();

        if (pm.getPlugin("PlaceholderAPI") == null) {
            getLogger().warning("Could not find softdependency: PlaceholderAPI");
            pm.disablePlugin(this);
        }

        if (pm.getPlugin("LuckPerms") == null) {
            getLogger().warning("Could not find dependency: LuckPerms");
            pm.disablePlugin(this);
        }

        registerPlaceholders();

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) luckPerms = provider.getProvider();
    }

    private void initializeFiles() {
        if (!getDataFolder().exists()) getDataFolder().mkdirs();
        if (!configFile.exists()) saveResource("config.yml", false);
        if (!langFile.exists()) saveResource("lang.yml", false);
        if (!ranksFile.exists()) saveResource("ranks.yml", false);

        forceReload();
    }

    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new JoinListener(), this);
        pm.registerEvents(new ChatListener(), this);
    }

    private void registerCommands() {
        getCommand("reload").setExecutor(new Reload());
        getCommand("setrank").setExecutor(new SetRank());
        getCommand("delrank").setExecutor(new DelRank());
    }

    private void registerPlaceholders() { new Rank().register(); }

    public void reloadConfigs() {
        try {
            config.save(configFile);
            lang.save(langFile);
            ranks.save(ranksFile);

            config.load(configFile);
            lang.load(langFile);
            ranks.load(ranksFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void forceReload() {
        try {
            config.load(configFile);
            lang.load(langFile);
            ranks.load(ranksFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    @Override
    public FileConfiguration getConfig() { return config; }
    public FileConfiguration getLangConfig() { return lang; }
    public FileConfiguration getRanksConfig() { return ranks; }
    public LuckPerms getLuckPerms() { return luckPerms; }

    public String getAuthor() { return getDescription().getAuthors().get(0); }
    public String getVersion() { return getDescription().getVersion(); }

    @Override
    public void onDisable() { instance = null; }
}
