package me.lewd.stencilutilities;

import me.lewd.stencilutilities.listeners.ChatListener;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class Main extends JavaPlugin {
    public static Main instance;

    private FileConfiguration config = new YamlConfiguration();

    @Override
    public void onEnable() {
        instance = this;

        initializeFiles();
        registerEvents();
    }

    private void initializeFiles() {
        File configFile = new File(getDataFolder(), "config.yml");

        if (!getDataFolder().exists()) getDataFolder().mkdirs();
        if (!configFile.exists()) saveResource("config.yml", false);

        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new ChatListener(), this);
    }

    @Override
    public void onDisable() { instance = null; }
}
