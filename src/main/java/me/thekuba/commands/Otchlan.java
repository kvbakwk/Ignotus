package me.thekuba.commands;

import java.util.ArrayList;
import java.util.Arrays;
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

public class Otchlan implements CommandExecutor, TabCompleter {
  private final Ignotus plugin = (Ignotus) Bukkit.getServer().getPluginManager().getPlugin("Ignotus");
  private final FileConfiguration config = this.plugin.getConfig();

  private static final String[] COMMANDS = new String[0];

  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (!(sender instanceof Player))
      return true; 
    if (this.plugin.clear != null) {
      Player player = (Player) sender;
      if (    this.plugin.clear.getTime() < this.config.getInt("abyss.interval") - this.config.getInt("abyss.lookable") &&
              this.config.getInt("abyss.lookable") > 0) {
        player.sendMessage(this.config.getString("messages.abyss-closed").replace("{1}", Integer.toString(this.plugin.clear.getTime())));
        return true;
      } 
      player.openInventory(this.plugin.abyssInv.get(0));
    }
    return true;
  }
  
  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
    List<String> completions = new ArrayList<>();
    StringUtil.copyPartialMatches(args[0], Arrays.asList(COMMANDS), completions);
    return completions;
  }
}
