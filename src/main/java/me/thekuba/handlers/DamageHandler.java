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
import org.bukkit.plugin.Plugin;

public class DamageHandler implements Listener {
  private Map<Player, Long> delay = new HashMap<>();
  
  private final Ignotus plugin = (Ignotus) Bukkit.getServer().getPluginManager().getPlugin("Ignotus");
  
  private final FileConfiguration config = this.plugin.getConfig();
  
  public DamageHandler(Ignotus plugin) {
    Bukkit.getPluginManager().registerEvents(this, (Plugin)plugin);
  }
  
  @EventHandler
  public void onDamage(EntityDamageEvent event) {
    if (!event.getEntityType().equals(EntityType.PLAYER))
      return; 
    Player player = Bukkit.getServer().getPlayer(event.getEntity().getUniqueId());
    if (!this.delay.containsKey(player)) {
      this.delay.put(player, Long.valueOf(System.currentTimeMillis() + (this.config.getInt("interact.antypvptime") * 1000)));
    } else {
      this.delay.replace(player, Long.valueOf(System.currentTimeMillis() + (this.config.getInt("interact.antypvptime") * 1000)));
    } 
  }
  
  @EventHandler
  public void onDamaged(EntityDamageByEntityEvent event) {
    if (!event.getEntityType().equals(EntityType.PLAYER))
      return; 
    Player player = Bukkit.getServer().getPlayer(event.getEntity().getUniqueId());
    if (!this.delay.containsKey(player)) {
      this.delay.put(player, Long.valueOf(System.currentTimeMillis() + (this.config.getInt("interact.antypvptime") * 1000)));
    } else {
      this.delay.replace(player, Long.valueOf(System.currentTimeMillis() + (this.config.getInt("interact.antypvptime") * 1000)));
    } 
  }
  
  public Map<Player, Long> getDelay() {
    return this.delay;
  }
}
