package me.thekuba.handlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.thekuba.Ignotus;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;

public class ClickInvHandler implements Listener {
  private Map<Player, Long> cooldown = new HashMap<>();
  
  private final Ignotus plugin = (Ignotus) Bukkit.getServer().getPluginManager().getPlugin("Ignotus");
  
  private final FileConfiguration config = this.plugin.getConfig();
  
  private final List<String> lore = this.config.getStringList("items.gift.lore-item");
  
  public ClickInvHandler(Ignotus plugin) {
    Bukkit.getPluginManager().registerEvents(this, (Plugin)plugin);
  }
  
  @EventHandler
  public void onClickInventory(InventoryClickEvent e) {
      e.setCancelled(true);
  }
}
