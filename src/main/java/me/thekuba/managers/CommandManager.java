package me.thekuba.managers;

import me.thekuba.Ignotus;
import me.thekuba.commands.*;
import org.bukkit.configuration.file.FileConfiguration;

public class CommandManager {
    private final Ignotus plugin;
    private final FileConfiguration config;

    public CommandManager(Ignotus plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();

        enableCommands();
    }

    private boolean enableCommands() {
        plugin.getCommand("set").setExecutor(new SetCommand());
        plugin.getCommand("setadmin").setExecutor(new SetAdminCommand());
        plugin.getCommand("abyss").setExecutor(new AbyssCommand());
        plugin.getCommand("self").setExecutor(new SelfCommand());
        plugin.getCommand("ignotus").setExecutor(new IgnotusCommand());
        return true;
    }
}
