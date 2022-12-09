package me.thekuba.handlers;

import java.util.List;
import java.util.UUID;

import me.thekuba.Ignotus;
import me.thekuba.items.ItemPersi;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class CloseInvHandler implements Listener {
  private final Ignotus plugin = (Ignotus) Bukkit.getServer().getPluginManager().getPlugin("Ignotus");
  private final FileConfiguration config = this.plugin.getConfig();
  
  public CloseInvHandler(Ignotus plugin) {
    Bukkit.getPluginManager().registerEvents(this, plugin);
  }
  
  @EventHandler
  public void onCloseInventory(InventoryCloseEvent e) {
    if(!checkItem(e.getInventory().getItem(0)))
      return;
    if(!checkItem(e.getInventory().getItem(37)))
      return;

    if ((new ItemPersi(e.getInventory().getItem(0))).getStringNBT("inventory").equals("interactPersival")
            && (new ItemPersi(e.getInventory().getItem(37))).getStringNBT("isConduit").equals("no")) {

      Player player = Bukkit.getPlayer(UUID.fromString((new ItemPersi(e.getInventory().getItem(0))).getStringNBT("p1")));
      ItemPersi item = new ItemPersi(e.getInventory().getItem(37));

      if (item.getStringNBT("hasFlagPersival") == "no")
        item.removeFlag(ItemFlag.HIDE_ATTRIBUTES); 
      if (item.getStringNBT("hasLorePersival") == "yes") {
        List<String> loreItem = item.getLore();
        for (String ignored : this.config.getStringList("items.gift.lore-item"))
          loreItem.remove(loreItem.size() - 1);
        item.setLore(loreItem, false, false, null);
      } else {
        item.setLore(null, false, false, null);
      }
      item.removeNBT("persiId");
      item.removeNBT("isConduit");
      item.removeNBT("PersiItem");
      item.removeNBT("hasFlagPersival");
      item.removeNBT("hasLorePersival");
      if(player.getInventory().firstEmpty() == -1)
        player.getWorld().dropItem(player.getLocation(), item);
      else
        player.getInventory().addItem(item);
    } 
  }




  private boolean checkItem(ItemStack item) {
    if(item == null)
      return false;
    if(item.getType() == Material.AIR)
      return false;
    if(new ItemPersi(item).getStringNBT("inventory") == null)
      return false;
    return true;
  }
}
