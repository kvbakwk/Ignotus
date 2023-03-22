package me.thekuba.handlers;

import me.thekuba.Ignotus;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PlayerJoinHandler implements Listener {
  private final Ignotus plugin;
  private FileConfiguration playersConfig;

  
  public PlayerJoinHandler(@NotNull Ignotus plugin) {
    this.plugin = plugin;
    if(com.google.common.base.Objects.equal(plugin.database, null))
      this.playersConfig = plugin.playersFile.getConfig();
    Bukkit.getPluginManager().registerEvents(this, plugin);
  }

  
  @EventHandler
  public void onPlayerJoin(@NotNull PlayerJoinEvent event) {

    final Player player = event.getPlayer();

    plugin.abyss.addPlayerToBar(player);

    if(!Objects.equals(plugin.database, null)) {
      if (!plugin.database.isPlayer(player))
        plugin.database.addPlayer(player);
    }
    else
      if (!this.playersConfig.contains("players." + player.getUniqueId() + ".nick")) {
        this.playersConfig.set("players." + player.getUniqueId() + ".nick", player.getName());
        this.playersConfig.set("players." + player.getUniqueId() + ".instagram", "");
        this.playersConfig.set("players." + player.getUniqueId() + ".youtube", "");
        this.playersConfig.set("players." + player.getUniqueId() + ".twitch", "");
        this.playersConfig.set("players." + player.getUniqueId() + ".discord", "");
        this.playersConfig.set("players." + player.getUniqueId() + ".snapchat", "");
        this.playersConfig.set("players." + player.getUniqueId() + ".status", "off");
        this.plugin.playersFile.saveConfig();
      }


    if (this.plugin.getConfig().getBoolean("nametags.enable"))
      this.plugin.nametag.addNametag(player);

  }

}
