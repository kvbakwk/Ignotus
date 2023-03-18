package me.thekuba.handlers;

import java.util.HashMap;
import java.util.Map;

import me.thekuba.Ignotus;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageHandler implements Listener {
  private final FileConfiguration config;

  private final Map<Player, Long> delay = new HashMap<>();

  
  public DamageHandler(Ignotus plugin) {
    this.config = plugin.getConfig();
    Bukkit.getPluginManager().registerEvents(this, plugin);
  }

  
  @EventHandler
  public void onDamage(EntityDamageEvent event) {
    if (!event.getEntityType().equals(EntityType.PLAYER))
      return; 
    Player player = Bukkit.getServer().getPlayer(event.getEntity().getUniqueId());
    if (!this.delay.containsKey(player)) {
      this.delay.put(player, System.currentTimeMillis() + (this.config.getInt("interact.antypvptime") * 1000L));
    } else {
      this.delay.replace(player, System.currentTimeMillis() + (this.config.getInt("interact.antypvptime") * 1000L));
    } 
  }
  @EventHandler
  public void onDamaged(EntityDamageByEntityEvent event) {
    if (!event.getEntityType().equals(EntityType.PLAYER))
      return; 
    Player player = Bukkit.getServer().getPlayer(event.getEntity().getUniqueId());
    if (!this.delay.containsKey(player)) {
      this.delay.put(player, System.currentTimeMillis() + (this.config.getInt("interact.antypvptime") * 1000L));
    } else {
      this.delay.replace(player, System.currentTimeMillis() + (this.config.getInt("interact.antypvptime") * 1000L));
    } 
  }


  public Map<Player, Long> getDelay() {
    return this.delay;
  }
}
