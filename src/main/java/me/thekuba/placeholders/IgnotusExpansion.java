package me.thekuba.placeholders;

import de.myzelyam.api.vanish.VanishAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.thekuba.Ignotus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class IgnotusExpansion extends PlaceholderExpansion {
  private Ignotus plugin;
  
  @NotNull
  public String getAuthor() {
    return "thekuba";
  }
  @NotNull
  public String getIdentifier() {
    return "ignotus";
  }
  @NotNull
  public String getVersion() {
    return "1.3";
  }
  public String getRequiredPlugin() {
    return "Ignotus";
  }
  public boolean canRegister() {
    return ((this.plugin = (Ignotus) Bukkit.getPluginManager().getPlugin(getRequiredPlugin())) != null);
  }
  
  public String onRequest(OfflinePlayer player, String params) {
    if (params.equalsIgnoreCase("instagram")) {
      String instagram;
      if(Objects.equals(plugin.database, null))
        instagram = this.plugin.playersFile.getConfig().getString("players." + player.getUniqueId() + ".instagram");
      else
        instagram = this.plugin.database.getValue(Bukkit.getOfflinePlayer(player.getUniqueId()), "instagram");
      if (instagram == null)
        return this.plugin.getConfig().getString("items.media.if-empty"); 
      if (instagram.equals(""))
        return this.plugin.getConfig().getString("items.media.if-empty"); 
      return ChatColor.translateAlternateColorCodes('&', instagram);
    } 
    if (params.equalsIgnoreCase("youtube")) {
      String youtube;
      if(Objects.equals(plugin.database, null))
        youtube = this.plugin.playersFile.getConfig().getString("players." + player.getUniqueId() + ".youtube");
      else
        youtube = this.plugin.database.getValue(Bukkit.getOfflinePlayer(player.getUniqueId()), "youtube");
      if (youtube == null)
        return this.plugin.getConfig().getString("items.media.if-empty"); 
      if (youtube.equals(""))
        return this.plugin.getConfig().getString("items.media.if-empty"); 
      return ChatColor.translateAlternateColorCodes('&', youtube);
    } 
    if (params.equalsIgnoreCase("twitch")) {
      String twitch;
      if(Objects.equals(plugin.database, null))
        twitch = this.plugin.playersFile.getConfig().getString("players." + player.getUniqueId() + ".twitch");
      else
        twitch = this.plugin.database.getValue(Bukkit.getOfflinePlayer(player.getUniqueId()), "twitch");
      if (twitch == null)
        return this.plugin.getConfig().getString("items.media.if-empty"); 
      if (twitch.equals(""))
        return this.plugin.getConfig().getString("items.media.if-empty"); 
      return ChatColor.translateAlternateColorCodes('&', twitch);
    } 
    if (params.equalsIgnoreCase("discord")) {
      String discord;
      if(Objects.equals(plugin.database, null))
        discord = this.plugin.playersFile.getConfig().getString("players." + player.getUniqueId() + ".discord");
      else
        discord = this.plugin.database.getValue(Bukkit.getOfflinePlayer(player.getUniqueId()), "discord");
      if (discord == null)
        return this.plugin.getConfig().getString("items.media.if-empty"); 
      if (discord.equals(""))
        return this.plugin.getConfig().getString("items.media.if-empty"); 
      return ChatColor.translateAlternateColorCodes('&', discord);
    } 
    if (params.equalsIgnoreCase("snapchat")) {
      String snapchat;
      if(Objects.equals(plugin.database, null))
        snapchat = this.plugin.playersFile.getConfig().getString("players." + player.getUniqueId() + ".snapchat");
      else
        snapchat = this.plugin.database.getValue(Bukkit.getOfflinePlayer(player.getUniqueId()), "snapchat");
      if (snapchat == null)
        return this.plugin.getConfig().getString("items.media.if-empty"); 
      if (snapchat.equals(""))
        return this.plugin.getConfig().getString("items.media.if-empty"); 
      return ChatColor.translateAlternateColorCodes('&', snapchat);
    }
    if (params.equalsIgnoreCase("status")) {
      String status;
      if(Objects.equals(plugin.database, null))
        status = this.plugin.playersFile.getConfig().getString("players." + player.getUniqueId() + ".status");
      else
        status = this.plugin.database.getValue(Bukkit.getOfflinePlayer(player.getUniqueId()), "status");
      if (status == null)
        return this.plugin.getConfig().getString("items.media.if-empty");
      if (status.equals(""))
        return this.plugin.getConfig().getString("items.media.if-empty");
      return ChatColor.translateAlternateColorCodes('&', status);
    }
    if (params.equalsIgnoreCase("abyss3")) {
      if (this.plugin.abyss != null) {
        String time = Integer.toString(this.plugin.abyss.getTime());
        if (time.length() == 3)
          return time; 
        if (time.length() == 2)
          return " " + time; 
        return "  " + time;
      } 
      return "Abyss is disabled.";
    }
    if (params.equalsIgnoreCase("abyss")) {
      if (this.plugin.abyss != null) {
        return Integer.toString(this.plugin.abyss.getTime());
      }
      return "Abyss is disabled.";
    }
    if (params.equalsIgnoreCase("vanish")) {
      int playersOnline = Bukkit.getServer().getOnlinePlayers().size();
      int vanishPlayers = VanishAPI.getInvisiblePlayers().size();
      return Integer.toString(playersOnline - vanishPlayers);
    } 
    return null;
  }
}
