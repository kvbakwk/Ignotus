package me.thekuba.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import me.thekuba.Ignotus;
import me.thekuba.IgnotusCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class SetCmd extends IgnotusCommand {
  private final FileConfiguration playersConfig = this.plugin.playersFile.getConfig();

  private static final String[] COMMANDS = new String[] { "instagram", "youtube", "twitch", "discord", "snapchat", "status" };

  public SetCmd(Ignotus plugin, String permission) {
    super(plugin, permission);
  }

  public boolean command(CommandSender sender, Command cmd, String label, String[] args) {
    if (!(sender instanceof Player))
      return true;

    int i, longMax;
    String argument0, argument1;
    StringBuilder argument1B;
    Player player = (Player)sender;

    switch (args.length) {

      default:
        argument0 = args[0].toLowerCase();
        argument1B = new StringBuilder();
        for (i = 1; i < args.length; i++) {
          if (i != args.length - 1)
            argument1B.append(args[i]).append(" ");
          else
            argument1B.append(args[i]);
        } 
        argument1 = argument1B.toString();

        longMax = this.config.getInt("interact.media-long");

        if (argument0.equals("instagram") || argument0.equals("youtube") || argument0.equals("twitch") || argument0.equals("discord") || argument0.equals("snapchat") || argument0.equals("status")) {
          if (argument1.length() >= longMax)
            player.sendMessage(plugin.colorCodes(this.messagesConfig.getString("commands.invalid-args.set-media-name-toolong")
                    .replace("{1}", Integer.toString(longMax))));
          else {
            player.sendMessage(plugin.colorCodes(this.messagesConfig.getString("commands.success.set")
                    .replace("{1}", argument1)
                    .replace("{0}", args[0].toLowerCase())));
            this.playersConfig.set("players." + player.getUniqueId() + "." + argument0, argument1);
            this.plugin.playersFile.saveConfig();
            return true;
          } 
        } else
          player.sendMessage(plugin.colorCodes(this.messagesConfig.getString("commands.invalid-args.set-unknown-media")));
        return true;

      case 1:
        player.sendMessage(plugin.colorCodes(this.messagesConfig.getString("commands.usage.schema")
                .replace("{0}", this.messagesConfig.getString("commands.usage.set")
                        .replace("{1}", args[0]))));
        return true;

      case 0:
        break;
    } 
    player.sendMessage(plugin.colorCodes(this.messagesConfig.getString("commands.usage.schema")
            .replace("{0}", this.messagesConfig.getString("commands.usage.set")
                    .replace("{0}", "<media>"))));
    return true;
  }
  
  public List<String> tabComplete(CommandSender sender, Command command, String alias, String[] args) {

    List<String> completions = new ArrayList<>();
    Player player = (Player)sender;

    if (args.length == 1)
      StringUtil.copyPartialMatches(args[0], Arrays.asList(COMMANDS), completions);
    else if (args.length == 2) {
      String tab = this.playersConfig.getString("players." + player.getUniqueId() + "." + args[0].toLowerCase());
      if (tab == null)
        tab = ""; 
      StringUtil.copyPartialMatches(args[1], Collections.singletonList(tab), completions);
    }

    return completions;
  }

}
