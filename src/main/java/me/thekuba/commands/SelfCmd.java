package me.thekuba.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.thekuba.Ignotus;
import me.thekuba.IgnotusCommand;
import me.thekuba.inventories.InteractInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class SelfCmd extends IgnotusCommand {
  private static final String[] COMMANDS = new String[0];

  public SelfCmd(Ignotus plugin, String permission) {
    super(plugin, permission);
  }

  public boolean command(CommandSender sender, Command cmd, String label, String[] args) {
    if (!(sender instanceof Player))
      return true; 

    Player player = (Player)sender;

    if (this.config.getBoolean("interact.enable"))
      InteractInventory.openInv(player, player);

    return true;

  }
  
  public List<String> tabComplete(CommandSender sender, Command command, String alias, String[] args) {
    List<String> completions = new ArrayList<>();
    StringUtil.copyPartialMatches(args[0], Arrays.asList(COMMANDS), completions);
    return completions;
  }

}
