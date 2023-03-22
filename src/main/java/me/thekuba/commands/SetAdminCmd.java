package me.thekuba.commands;

import java.util.*;

import me.thekuba.Ignotus;
import me.thekuba.IgnotusCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.StringUtil;

public class SetAdminCmd extends IgnotusCommand {
  private FileConfiguration playersConfig;
  private static final String[] COMMANDS = new String[] { "instagram", "youtube", "twitch", "discord", "snapchat", "status" };

  public SetAdminCmd(Ignotus plugin, String permission) {
    super(plugin, permission);
    if(Objects.equals(plugin.database, null))
      this.playersConfig = plugin.playersFile.getConfig();
  }

  public boolean command(CommandSender sender, Command cmd, String label, String[] args) {

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
          sender.sendMessage(plugin.colorCodes(this.messagesConfig.getString("commands.success.setadmin")
                  .replace("{0}", args[0].toLowerCase())
                  .replace("{1}", argument1)
                  .replace("{2}", argument2)));
          if(Objects.equals(plugin.database, null)) {
            this.playersConfig.set("players." + Bukkit.getPlayer(argument1).getUniqueId() + "." + argument0, argument2);
            this.plugin.playersFile.saveConfig();
          } else {
            this.plugin.database.setValue(Bukkit.getOfflinePlayer(argument1), argument0, argument2);
          }
        } else
          sender.sendMessage(plugin.colorCodes(this.messagesConfig.getString("commands.invalid-args.set-unknown-media")));
        return true;

      case 2:
        sender.sendMessage(plugin.colorCodes(this.messagesConfig.getString("commands.usage.schema")
                .replace("{0}", this.messagesConfig.getString("commands.usage.setadmin")
                        .replace("{0}", args[0])
                        .replace("{1}", args[1]))));
        return true;

      case 1:
        sender.sendMessage(plugin.colorCodes(this.messagesConfig.getString("commands.usage.schema")
                .replace("{0}", this.messagesConfig.getString("commands.usage.setadmin")
                        .replace("{0}", args[0]).replace("{1}", "<nick>"))));
        return true;

      case 0:
        break;
    } 
    sender.sendMessage(plugin.colorCodes(this.messagesConfig.getString("commands.usage.schema")
            .replace("{0}", this.messagesConfig.getString("commands.usage.setadmin")
                    .replace("{0}", "<media>").replace("{1}", "<nick>"))));
    return true;
  }
  
  public List<String> tabComplete(CommandSender sender, Command command, String alias, String[] args) {

    List<String> completions = new ArrayList<>();

    if (args.length == 1)
      StringUtil.copyPartialMatches(args[0], Arrays.asList(COMMANDS), completions);
    else if (args.length == 2) {
      List<String> players = new ArrayList<>();
      for (OfflinePlayer playerOffline : Bukkit.getServer().getOfflinePlayers())
        players.add(playerOffline.getName()); 
      StringUtil.copyPartialMatches(args[1], players, completions);
    } else if (args.length == 3) {
      String tab;
      if(java.util.Objects.equals(plugin.database, null)) {
        tab = this.playersConfig.getString("players." + Bukkit.getPlayer(args[1]).getUniqueId() + "." + args[0].toLowerCase());
      } else {
        tab = this.plugin.database.getValue(Bukkit.getOfflinePlayer(args[1]), args[0].toLowerCase());
      }
      if (tab == null)
        tab = ""; 
      StringUtil.copyPartialMatches(args[2], Collections.singletonList(tab), completions);
    }

    return completions;
  }

}
