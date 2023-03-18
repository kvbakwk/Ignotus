package me.thekuba.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.thekuba.Ignotus;
import me.thekuba.IgnotusCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class AbyssCmd extends IgnotusCommand {
  private static final String[] COMMANDS = new String[0];

  public AbyssCmd(Ignotus plugin, String permission) {
    super(plugin, permission);
  }

  public boolean command(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player))
      return true; 
    if (this.plugin.clear != null) {
      Player player = (Player) sender;
      if (    this.plugin.clear.getTime() < this.config.getInt("abyss.interval") - this.config.getInt("abyss.lookable") &&
              this.config.getInt("abyss.lookable") > 0) {
        player.sendMessage(plugin.colorCodes(messagesConfig.getString("abyss.closed")
                .replace("{1}", Integer.toString(this.plugin.clear.getTime()))));
        return true;
      } 
      player.openInventory(this.plugin.abyssInv.get(0));
    }
    return true;
  }
  
  public List<String> tabComplete(CommandSender sender, Command command, String alias, String[] args) {
    List<String> completions = new ArrayList<>();
    StringUtil.copyPartialMatches(args[0], Arrays.asList(COMMANDS), completions);
    return completions;
  }

}
