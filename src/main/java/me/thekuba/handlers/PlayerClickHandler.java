package me.thekuba.handlers;

import me.thekuba.Ignotus;
import me.thekuba.inventories.InteractInventory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;

public class PlayerClickHandler implements Listener {
  private final Ignotus plugin;
  
  public PlayerClickHandler(Ignotus plugin) {
    this.plugin = plugin;
    Bukkit.getPluginManager().registerEvents(this, (Plugin)plugin);
  }
  
  @EventHandler
  public void onPlayerInteract(PlayerInteractEntityEvent event) {
    if (this.plugin.getConfig().getBoolean("interact.enable")) {
      Player playerOne = event.getPlayer();
      Player playerTwo = Bukkit.getPlayer(event.getRightClicked().getUniqueId());
      if (this.plugin.pvp.getDelay() != null && this.plugin.pvp.getDelay().get(playerOne) != null && this.plugin.pvp.getDelay().get(playerTwo) != null && ((
        (Long)this.plugin.pvp.getDelay().get(playerOne)).longValue() > System.currentTimeMillis() || ((Long)this.plugin.pvp.getDelay().get(playerTwo)).longValue() > System.currentTimeMillis()))
        return; 
      if (!(playerTwo instanceof Player))
        return; 
      if (event.getHand() == EquipmentSlot.OFF_HAND)
        return; 
      InteractInventory.openInv(playerOne, playerTwo);
    } 
  }
}
