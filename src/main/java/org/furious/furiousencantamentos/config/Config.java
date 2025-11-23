package org.furious.furiousencantamentos.config;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.furious.furiousencantamentos.FuriousEncantamentos;

import java.io.File;

public abstract class Config {

    private FileConfiguration fileConfiguration;
    private File file;
    private final FuriousEncantamentos plugin;
    private final String fileName;

    public Config(FuriousEncantamentos plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
        init();
    }

    private void init() {
        file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            try {
                plugin.saveResource(fileName, false);
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "Nao foi possivel salvar o arquivo  " + fileName);
                e.printStackTrace();
            }
        }
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public final void reloadConfig() {
        try {
            fileConfiguration = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final FileConfiguration getCustomConfig() {
        return fileConfiguration;
    }

}
