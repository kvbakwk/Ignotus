package me.thekuba.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import me.thekuba.Ignotus;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

public class SetAdminCommand implements CommandExecutor, TabCompleter {
  private final Ignotus plugin = (Ignotus) Bukkit.getServer().getPluginManager().getPlugin("Ignotus");
  private final FileConfiguration config = this.plugin.getConfig();
  private final FileConfiguration playersConfig = this.plugin.playersFile.getConfig();

  private static final String[] COMMANDS = new String[] { "Instagram", "YouTube", "Twitch", "Discord", "Snapchat", "Status" };

  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

    int i;
    String argument0, argument1, argument2;
    StringBuilder argument2B;

    switch (args.length) {

      default:
        argument0 = args[0].toLowerCase();
        argument1 = args[1];
        argument2B = new StringBuilder();
        for (i = 2; i < args.length; i++) {
          if (i != args.length - 1) {
            argument2B.append(args[i]).append(" ");
          } else {
            argument2B.append(args[i]);
          } 
        } 
        argument2 = argument2B.toString();

        if (argument0.equals("instagram") || argument0.equals("youtube") || argument0.equals("twitch") || argument0.equals("discord") || argument0.equals("snapchat") || argument0.equals("status")) {
          sender.sendMessage(this.config.getString("messages.success-admin")
                  .replace("{0}", args[0].toLowerCase())
                  .replace("{1}", argument1)
                  .replace("{2}", argument2));
          this.playersConfig.set("players." + Bukkit.getPlayer(argument1).getUniqueId() + "." + argument0, argument2);
          this.plugin.playersFile.saveConfig();
        } else
          sender.sendMessage(this.config.getString("messages.unknown"));
        return true;

      case 2:
        sender.sendMessage(this.config.getString("messages.usage-admin")
            .replace("{0}", args[0])
            .replace("{1}", args[1]));
        return true;

      case 1:
        sender.sendMessage(this.config.getString("messages.usage-admin")
                .replace("{0}", args[0]).replace("{1}", "<nick>"));
        return true;

      case 0:
        break;
    } 
    sender.sendMessage(this.config.getString("messages.usage-admin")
            .replace("{0}", "<media>").replace("{1}", "<nick>"));
    return true;
  }
  
  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {

    List<String> completions = new ArrayList<>();

    if (args.length == 1)
      StringUtil.copyPartialMatches(args[0], Arrays.asList(COMMANDS), completions);
    else if (args.length == 2) {
      List<String> players = new ArrayList<>();
      for (OfflinePlayer playerOffline : Bukkit.getServer().getOfflinePlayers())
        players.add(playerOffline.getName()); 
      StringUtil.copyPartialMatches(args[1], players, completions);
    } else if (args.length == 3) {
      String tab = this.playersConfig.getString("players." + Bukkit.getPlayer(args[1]).getUniqueId() + "." + args[0].toLowerCase());
      Bukkit.getLogger().info(tab);
      if (tab == null)
        tab = ""; 
      StringUtil.copyPartialMatches(args[2], Collections.singletonList(tab), completions);
    }

    return completions;
  }

}
