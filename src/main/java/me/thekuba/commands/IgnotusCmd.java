package me.thekuba.commands;

import me.thekuba.Ignotus;
import me.thekuba.IgnotusCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IgnotusCmd extends IgnotusCommand {

  private static final String[] COMMANDS = new String[0];

  public IgnotusCmd(Ignotus plugin, String permission) {
    super(plugin, permission);
  }

  public boolean command(CommandSender sender, Command command, String label, String[] args) {

    for(String line : this.messagesConfig.getStringList("help")) {
      sender.sendMessage(plugin.colorCodes(line));
    }

    return true;
  }
  
  public List<String> tabComplete(CommandSender sender, Command command, String alias, String[] args) {
    List<String> completions = new ArrayList<>();
    StringUtil.copyPartialMatches(args[0], Arrays.asList(COMMANDS), completions);
    return completions;
  }

}
