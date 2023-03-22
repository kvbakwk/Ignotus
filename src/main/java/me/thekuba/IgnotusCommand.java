package me.thekuba;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class IgnotusCommand implements CommandExecutor, TabCompleter {

    public final Ignotus plugin;
    public final FileConfiguration config, groupsConfig, messagesConfig;
    public final String permission;

    public IgnotusCommand(Ignotus plugin, String permission) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.groupsConfig = plugin.groupsFile.getConfig();
        this.messagesConfig = plugin.messagesFile.getConfig();
        this.permission = permission;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender.isOp() || sender.hasPermission(permission))
            return command(sender, command, label, args);
        else
            sender.sendMessage(plugin.colorCodes(messagesConfig.getString("no-permission")));
        return true;
    }

    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(sender.hasPermission(permission))
            return tabComplete(sender, command, alias, args);
        else
            return new ArrayList<>();
    }

    public abstract boolean command(CommandSender sender, Command command, String label, String[] args);
    public abstract List<String> tabComplete(CommandSender sender, Command command, String alias, String[] args);

}
