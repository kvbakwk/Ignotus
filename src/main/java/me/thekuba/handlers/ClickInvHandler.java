package me.thekuba.handlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.thekuba.Ignotus;
import me.thekuba.items.ItemPersi;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ClickInvHandler implements Listener {
  private final Ignotus plugin = (Ignotus) Bukkit.getServer().getPluginManager().getPlugin("Ignotus");
  private final FileConfiguration config = this.plugin.getConfig();

  private Map<Player, Long> cooldown = new HashMap<>();

  public ClickInvHandler(Ignotus plugin) {
    Bukkit.getPluginManager().registerEvents(this, (Plugin)plugin);
  }
  
  @EventHandler
  public void onClickInventory(InventoryClickEvent e) {
      if(!checkItem(e.getWhoClicked().getOpenInventory().getItem(0)))
        return;
      if(!checkItem(e.getCurrentItem()))
        return;

      Player player = (Player) e.getWhoClicked();
      Player player2 = Bukkit.getPlayer(UUID.fromString(new ItemPersi(e.getWhoClicked().getOpenInventory().getItem(0)).getStringNBT("p2")));

      if(new ItemPersi(e.getWhoClicked().getOpenInventory().getItem(0)).getStringNBT("inventory") == "abyssPersival") {
        e.setCancelled(true);
      }

      else if(new ItemPersi(e.getWhoClicked().getOpenInventory().getItem(0)).getStringNBT("inventory") == "interactPersival") {
        ItemPersi item = new ItemPersi(e.getCurrentItem());

        switch (item.getStringNBT("persiId")) {
          case "profile":
            player.sendMessage("Profile");
            if(e.getClick().isLeftClick() && !config.getString("items.profile.commandL").equals(""))
              player.performCommand(config.getString("items.profile.commandL").replace("{1}", player2.getName()));
            if(e.getClick().isRightClick() && !config.getString("items.profile.commandR").equals(""))
              player.performCommand(config.getString("items.profile.commandR").replace("{1}", player2.getName()));
            e.setCancelled(true);
            return;
          case "i0":
            player.sendMessage("Item 0");
            if(e.getClick().isLeftClick() && !config.getString("items.item0.commandL").equals(""))
              player.performCommand(config.getString("items.item0.commandL").replace("{1}", player2.getName()));
            if(e.getClick().isRightClick() && !config.getString("items.item0.commandR").equals(""))
              player.performCommand(config.getString("items.item0.commandR").replace("{1}", player2.getName()));
            e.setCancelled(true);
            return;
          case "i1":
            player.sendMessage("Item 1");
            if(e.getClick().isLeftClick() && !config.getString("items.item1.commandL").equals(""))
              player.performCommand(config.getString("items.item1.commandL").replace("{1}", player2.getName()));
            if(e.getClick().isRightClick() && !config.getString("items.item1.commandR").equals(""))
              player.performCommand(config.getString("items.item1.commandR").replace("{1}", player2.getName()));
            e.setCancelled(true);
            return;
          case "i2":
            player.sendMessage("Item 2");
            if(e.getClick().isLeftClick() && !config.getString("items.item2.commandL").equals(""))
              player.performCommand(config.getString("items.item2.commandL").replace("{1}", player2.getName()));
            if(e.getClick().isRightClick() && !config.getString("items.item2.commandR").equals(""))
              player.performCommand(config.getString("items.item2.commandR").replace("{1}", player2.getName()));
            e.setCancelled(true);
            return;
          case "conduit":
            player.sendMessage("Conduit");
            if(item.getStringNBT("isConduit").equals("yes"))
              e.setCancelled(true);
            else {
              if(e.getClick().isShiftClick() && e.getClick().isLeftClick()) {
                e.setCancelled(true);
                player2.getInventory().addItem(item);
                e.getWhoClicked().getOpenInventory().setItem(37, item);
                player.sendMessage(config.getString("messages.gift-give").replace("{1}", player2.getName()));
                player2.sendMessage(config.getString("messages.gift-get").replace("{1}", player.getName()));
              } else {
                e.getWhoClicked().getOpenInventory().setItem(37, item);
                e.getWhoClicked().getInventory().addItem(item);
              }
            }
            return;
          default:
            e.setCancelled(true);
            e.getWhoClicked().getInventory().remove(e.getCurrentItem());
            item.setStringNBT("persiId", "conduit");
            item.setStringNBT("isConduit", "no");
            e.getWhoClicked().getOpenInventory().setItem(37, item);
            return;
          case "blank":
            e.setCancelled(true);
        }
      }
  }

  private boolean checkItem(ItemStack item) {
    if(item == null)
      return false;
    else if(item.getType() == Material.AIR)
      return false;
    else if(new ItemPersi(item).getStringNBT("inventory") == null)
      return false;
    else
      return true;
  }
}
