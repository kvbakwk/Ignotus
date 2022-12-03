package me.thekuba.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import me.thekuba.Ignotus;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

public class Set implements CommandExecutor, TabCompleter {
  private static final String[] COMMANDS = new String[] { "Instagram", "YouTube", "Twitch", "Discord", "Snapchat", "Status" };
  
  @NotNull
  private final Ignotus plugin = (Ignotus) Bukkit.getServer().getPluginManager().getPlugin("Ignotus");
  
  private final FileConfiguration config = this.plugin.getConfig();
  
  private final FileConfiguration playersConfig = this.plugin.playersFile.getConfig();
  
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
    String argument0;
    StringBuilder argument1B;
    int i;
    String argument1;
    int longMax;
    String longMaxString;
    if (!(sender instanceof Player))
      return true; 
    Player player = (Player)sender;
    switch (args.length) {
      default:
        argument0 = args[0].toLowerCase();
        argument1B = new StringBuilder();
        for (i = 1; i < args.length; i++) {
          if (i != args.length - 1) {
            argument1B.append(args[i]).append(" ");
          } else {
            argument1B.append(args[i]);
          } 
        } 
        argument1 = argument1B.toString();
        longMax = this.config.getInt("messages.long");
        longMaxString = Integer.toString(longMax);
        if (argument0.equals("instagram") || argument0.equals("youtube") || argument0.equals("twitch") || argument0.equals("discord") || argument0.equals("snapchat") || argument0.equals("status")) {
          if (argument1.length() >= longMax) {
            player.sendMessage(this.config.getString("messages.toolong").replace("{1}", longMaxString));
          } else {
            player.sendMessage(this.config.getString("messages.success").replace("{1}", argument1)
                .replace("{0}", args[0].toLowerCase() + "a"));
            this.playersConfig.set("players." + player.getUniqueId() + "." + argument0, argument1);
            this.plugin.playersFile.saveConfig();
            return true;
          } 
        } else {
          player.sendMessage(this.config.getString("messages.unknown"));
          return true;
        } 
        return true;
      case 1:
        player.sendMessage(this.config.getString("messages.usage").replace("{1}", args[0]));
        return true;
      case 0:
        break;
    } 
    player.sendMessage(this.config.getString("messages.usage").replace("{1}", "Instagram | YouTube | Twitch | Discord | Snapchat | Status"));
    return true;
  }
  
  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
    List<String> completions = new ArrayList<>();
    Player player = (Player)sender;
    if (args.length == 1) {
      StringUtil.copyPartialMatches(args[0], Arrays.asList(COMMANDS), completions);
    } else if (args.length == 2) {
      String tab = this.playersConfig.getString("players." + player.getUniqueId().toString() + "." + args[0].toLowerCase());
      if (tab == null)
        tab = ""; 
      StringUtil.copyPartialMatches(args[1], Collections.singletonList(tab), completions);
    } 
    return completions;
  }
}
