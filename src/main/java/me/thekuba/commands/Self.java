package me.thekuba.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.thekuba.Ignotus;
import me.thekuba.inventories.InteractInventory;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

public class Self implements CommandExecutor, TabCompleter {
  private final Ignotus plugin = (Ignotus) Bukkit.getServer().getPluginManager().getPlugin("Ignotus");
  private final FileConfiguration config = this.plugin.getConfig();

  private static final String[] COMMANDS = new String[0];


  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (!(sender instanceof Player))
      return true; 

    Player player = (Player)sender;

    if (this.config.getBoolean("interact.enable"))
      InteractInventory.openInv(player, player);

    return true;

  }
  
  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
    List<String> completions = new ArrayList<>();
    StringUtil.copyPartialMatches(args[0], Arrays.asList(COMMANDS), completions);
    return completions;
  }

}
