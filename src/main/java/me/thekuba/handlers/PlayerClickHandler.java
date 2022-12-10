package me.thekuba.handlers;

import me.thekuba.Ignotus;
import me.thekuba.inventories.InteractInventory;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

public class PlayerClickHandler implements Listener {
  private final Ignotus plugin;
  private final FileConfiguration config;


  public PlayerClickHandler(Ignotus plugin) {
    this.plugin = plugin;
    this.config = plugin.getConfig();
    Bukkit.getPluginManager().registerEvents(this, plugin);
  }
  
  @EventHandler
  public void onPlayerInteract(PlayerInteractEntityEvent event) {

    if (this.config.getBoolean("interact.enable")) {
      Player playerOne = event.getPlayer();
      Player playerTwo = Bukkit.getPlayer(event.getRightClicked().getUniqueId());
      if (this.plugin.pvp.getDelay() != null
              && this.plugin.pvp.getDelay().get(playerOne) != null
              && this.plugin.pvp.getDelay().get(playerTwo) != null
              && (this.plugin.pvp.getDelay().get(playerOne) > System.currentTimeMillis() || this.plugin.pvp.getDelay().get(playerTwo) > System.currentTimeMillis()))
        return; 
      if (playerTwo == null)
        return; 
      if (event.getHand() == EquipmentSlot.OFF_HAND)
        return;

      InteractInventory.openInv(playerOne, playerTwo);
    }

  }
}
