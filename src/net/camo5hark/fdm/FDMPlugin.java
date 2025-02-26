package net.camo5hark.fdm;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class FDMPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        reload();
        getServer().getPluginManager().registerEvents(new FDMListener(this), this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!label.equalsIgnoreCase("fdm")) {
            return false;
        }

        if (!args[0].equalsIgnoreCase("reload")) {
            return false;
        }

        reload();

        sender.sendMessage(ChatColor.GREEN + "Reloaded FunnyDeathMessages!");

        return true;
    }

    public void reload() {
        saveDefaultConfig();
        reloadConfig();
    }
}
