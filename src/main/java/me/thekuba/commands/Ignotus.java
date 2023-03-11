package me.thekuba.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ignotus implements CommandExecutor, TabCompleter {
  private final me.thekuba.Ignotus plugin = (me.thekuba.Ignotus) Bukkit.getServer().getPluginManager().getPlugin("Ignotus");
  private final FileConfiguration config = this.plugin.getConfig();

  private static final String[] COMMANDS = new String[0];


  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

    Player player = (Player) sender;
    player.sendMessage(this.config.getString("messages.help"));

    return true;
  }
  
  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
    List<String> completions = new ArrayList<>();
    StringUtil.copyPartialMatches(args[0], Arrays.asList(COMMANDS), completions);
    return completions;
  }

}
