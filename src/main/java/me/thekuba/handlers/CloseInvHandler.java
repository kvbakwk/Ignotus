package me.thekuba.handlers;

import de.tr7zw.nbtapi.NBTItem;
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
  
  private final List<String> lore = this.config.getStringList("items.gift.lore-item");
  
  public CloseInvHandler(Ignotus plugin) {
    Bukkit.getPluginManager().registerEvents(this, plugin);
  }
  
  @EventHandler
  public void onCloseInventory(InventoryCloseEvent event) {

    if (event.getInventory().getSize() > 9
            && event.getInventory().getItem(10) != null
            && event.getInventory().getItem(10).getType() != Material.AIR
            && (new ItemPersi(event.getInventory().getItem(10))).getStringNBT("inventory").equals("interPersival")
            && event.getInventory().getItem(37) != null
            && event.getInventory().getItem(37).getType() != Material.AIR
            && (new ItemPersi(event.getInventory().getItem(37))).getStringNBT("persiId").equals("give")) {

      Player player = Bukkit.getPlayer(UUID.fromString((new ItemPersi(event.getInventory().getItem(10))).getStringNBT("p1")));
      ItemPersi item = new ItemPersi(event.getInventory().getItem(37));
      if (item.getStringNBT("hasFlagPersival") == "no")
        item.removeFlag(ItemFlag.HIDE_ATTRIBUTES); 
      if (item.getStringNBT("hasLorePersival") == "yes") {
        List<String> loreItem = item.getLore();
        int loreItemSize = loreItem.size();
        for (int i = loreItemSize - 1; i > loreItemSize - this.lore.size(); i--)
          loreItem.remove(i); 
        item.setLore(loreItem, false, false, null);
      } else {
        item.setLore(null, false, false, null);
      } 
      item.removeNBT("hasLorePersival");
      item.removeNBT("hasFlagPersival");
      item.removeNBT("p1");
      item.removeNBT("p2");
      item.removeNBT("blocked");
      item.removeNBT("persiId");
      item.removeNBT("PersiItem");
      player.getInventory().addItem(new ItemStack[] { item });
    } 
  }
}
