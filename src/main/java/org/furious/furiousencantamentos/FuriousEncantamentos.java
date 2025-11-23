package org.furious.furiousencantamentos;

import lombok.Getter;
import net.minecraft.server.v1_8_R3.Enchantment;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.furious.furiousencantamentos.commands.ReloadPluginCommand;
import org.furious.furiousencantamentos.config.EnchantmentConfig;
import org.furious.furiousencantamentos.events.OpenEnchantmentTableEvent;

@Getter
public final class FuriousEncantamentos extends JavaPlugin {

    EnchantmentConfig enchantmentConfig;

    @Override
    public void onEnable() {
        enchantmentConfig = new EnchantmentConfig(this, "encantamentos.yml");

        registerEvent(new OpenEnchantmentTableEvent(this));
        getCommand("rlencantamentos").setExecutor(new ReloadPluginCommand(this));
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "[FuriousEncantamentos] Plugin ativado com sucesso!");
    }

    @Override
    public void onDisable() {
        enchantmentConfig.reloadConfig();
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[FuriousEncantamentos] desativado com sucesso!");
    }

    private void registerEvent(Listener event) {
        Bukkit.getPluginManager().registerEvents(event, this);
    }
}
