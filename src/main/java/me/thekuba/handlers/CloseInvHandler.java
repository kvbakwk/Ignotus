package me.thekuba.handlers;

import java.util.List;
import java.util.UUID;

import me.thekuba.Ignotus;
import me.thekuba.IgnotusItem;
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
  private final FileConfiguration config;


  public CloseInvHandler(Ignotus plugin) {
    this.config = plugin.getConfig();
    Bukkit.getPluginManager().registerEvents(this, plugin);
  }

  @EventHandler
  public void onCloseInventory(InventoryCloseEvent e) {
    if(!checkItem(e.getInventory().getItem(0)))
      return;
    if(e.getInventory().getSize() < 37 || !checkItem(e.getInventory().getItem(37)))
      return;

    // Interact Inventory Close
    if ((new IgnotusItem(e.getInventory().getItem(0))).getStringNBT("inventory").equals("interactPersival")
            && (new IgnotusItem(e.getInventory().getItem(37))).getStringNBT("isConduit").equals("no")) {

      Player player = Bukkit.getPlayer(UUID.fromString((new IgnotusItem(e.getInventory().getItem(0))).getStringNBT("p1")));
      IgnotusItem item = new IgnotusItem(e.getInventory().getItem(37));

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
    return item != null && item.getType() != Material.AIR && new IgnotusItem(item).getStringNBT("inventory") != null;
  }
}
