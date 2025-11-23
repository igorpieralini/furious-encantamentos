package org.furious.furiousencantamentos.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.furious.furiousencantamentos.FuriousEncantamentos;

public class ReloadPluginCommand implements CommandExecutor {

    private final FuriousEncantamentos furiousEncantamentos;
    public ReloadPluginCommand(FuriousEncantamentos furiousEncantamentos) {
        this.furiousEncantamentos = furiousEncantamentos;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(player.isOp()) {
                furiousEncantamentos.getEnchantmentConfig().reloadConfig();
                player.sendMessage(ChatColor.GREEN + "Arquivo de encantamentos recarregado.");
            }
        }
        return false;
    }
}
