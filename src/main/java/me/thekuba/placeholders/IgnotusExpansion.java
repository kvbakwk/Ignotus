package me.thekuba.placeholders;

import de.myzelyam.api.vanish.VanishAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.thekuba.Ignotus;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

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
    return "1.0.0";
  }
  public String getRequiredPlugin() {
    return "Ignotus";
  }
  public boolean canRegister() {
    return ((this.plugin = (Ignotus) Bukkit.getPluginManager().getPlugin(getRequiredPlugin())) != null);
  }
  
  public String onRequest(OfflinePlayer player, String params) {
    if (params.equalsIgnoreCase("instagram")) {
      String instagram = this.plugin.playersFile.getConfig().getString("players." + player.getUniqueId() + ".instagram");
      if (instagram == null)
        return this.plugin.getConfig().getString("items.media.if-empty"); 
      if (instagram.equals(""))
        return this.plugin.getConfig().getString("items.media.if-empty"); 
      return instagram;
    } 
    if (params.equalsIgnoreCase("youtube")) {
      String youtube = this.plugin.playersFile.getConfig().getString("players." + player.getUniqueId() + ".youtube");
      if (youtube == null)
        return this.plugin.getConfig().getString("items.media.if-empty"); 
      if (youtube.equals(""))
        return this.plugin.getConfig().getString("items.media.if-empty"); 
      return youtube;
    } 
    if (params.equalsIgnoreCase("twitch")) {
      String twitch = this.plugin.playersFile.getConfig().getString("players." + player.getUniqueId() + ".twitch");
      if (twitch == null)
        return this.plugin.getConfig().getString("items.media.if-empty"); 
      if (twitch.equals(""))
        return this.plugin.getConfig().getString("items.media.if-empty"); 
      return twitch;
    } 
    if (params.equalsIgnoreCase("discord")) {
      String discord = this.plugin.playersFile.getConfig().getString("players." + player.getUniqueId() + ".discord");
      if (discord == null)
        return this.plugin.getConfig().getString("items.media.if-empty"); 
      if (discord.equals(""))
        return this.plugin.getConfig().getString("items.media.if-empty"); 
      return discord;
    } 
    if (params.equalsIgnoreCase("snapchat")) {
      String discord = this.plugin.playersFile.getConfig().getString("players." + player.getUniqueId() + ".snapchat");
      if (discord == null)
        return this.plugin.getConfig().getString("items.media.if-empty"); 
      if (discord.equals(""))
        return this.plugin.getConfig().getString("items.media.if-empty"); 
      return discord;
    } 
    if (params.equalsIgnoreCase("abyss")) {
      if (this.plugin.clear != null) {
        String time = Integer.toString(this.plugin.clear.getTime());
        if (time.length() == 3)
          return time; 
        if (time.length() == 2)
          return " " + time; 
        return "  " + time;
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
