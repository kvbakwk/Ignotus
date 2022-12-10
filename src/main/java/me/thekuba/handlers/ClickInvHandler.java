package me.thekuba.handlers;

import java.util.*;

import me.thekuba.Ignotus;
import me.thekuba.items.ItemPersi;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ClickInvHandler implements Listener {
  private final Ignotus plugin = (Ignotus) Bukkit.getServer().getPluginManager().getPlugin("Ignotus");
  private final FileConfiguration config = this.plugin.getConfig();

  private final Map<Player, Long> cooldown = new HashMap<>();

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

      if(new ItemPersi(e.getWhoClicked().getOpenInventory().getItem(0)).getStringNBT("inventory") == "abyssPersival") {
        ItemPersi item = new ItemPersi(e.getCurrentItem());

        switch (item.getStringNBT("persiId")) {
          case "arrowLeft":
            e.setCancelled(true);
            e.getWhoClicked().openInventory(plugin.abyssInv.get(new ItemPersi(e.getWhoClicked().getOpenInventory().getItem(0)).getIntNBT("pagePersival") - 1));
            return;
          case "arrowRight":
            e.setCancelled(true);
            e.getWhoClicked().openInventory(plugin.abyssInv.get(new ItemPersi(e.getWhoClicked().getOpenInventory().getItem(0)).getIntNBT("pagePersival") + 1));
            return;
          case "blank":
            e.setCancelled(true);
            return;
          default:
            if(e.getWhoClicked().getInventory().equals(e.getClickedInventory()))
              e.setCancelled(true);
        }
      }

      else if(new ItemPersi(e.getWhoClicked().getOpenInventory().getItem(0)).getStringNBT("inventory") == "interactPersival") {
        ItemPersi item = new ItemPersi(e.getCurrentItem());
        Player player2 = Bukkit.getPlayer(UUID.fromString(new ItemPersi(e.getWhoClicked().getOpenInventory().getItem(0)).getStringNBT("p2")));

        switch (item.getStringNBT("persiId")) {
          case "profile":
            if(e.getClick().isLeftClick() && !config.getString("items.profile.commandL").equals(""))
              player.performCommand(config.getString("items.profile.commandL").replace("{1}", player2.getName()));
            if(e.getClick().isRightClick() && !config.getString("items.profile.commandR").equals(""))
              player.performCommand(config.getString("items.profile.commandR").replace("{1}", player2.getName()));
            e.setCancelled(true);
            return;
          case "i0":
            if(e.getClick().isLeftClick() && !config.getString("items.item0.commandL").equals(""))
              player.performCommand(config.getString("items.item0.commandL").replace("{1}", player2.getName()));
            if(e.getClick().isRightClick() && !config.getString("items.item0.commandR").equals(""))
              player.performCommand(config.getString("items.item0.commandR").replace("{1}", player2.getName()));
            e.setCancelled(true);
            return;
          case "i1":
            if(e.getClick().isLeftClick() && !config.getString("items.item1.commandL").equals(""))
              player.performCommand(config.getString("items.item1.commandL").replace("{1}", player2.getName()));
            if(e.getClick().isRightClick() && !config.getString("items.item1.commandR").equals(""))
              player.performCommand(config.getString("items.item1.commandR").replace("{1}", player2.getName()));
            e.setCancelled(true);
            return;
          case "i2":
            if(e.getClick().isLeftClick() && !config.getString("items.item2.commandL").equals(""))
              player.performCommand(config.getString("items.item2.commandL").replace("{1}", player2.getName()));
            if(e.getClick().isRightClick() && !config.getString("items.item2.commandR").equals(""))
              player.performCommand(config.getString("items.item2.commandR").replace("{1}", player2.getName()));
            e.setCancelled(true);
            return;
          case "conduit":
            if(item.getStringNBT("isConduit").equals("yes"))
              e.setCancelled(true);
            else {
              if(e.getClick().isShiftClick() && e.getClick().isLeftClick()) {
                e.setCancelled(true);
                if(cooldown.get(player) != null && cooldown.get(player) > System.currentTimeMillis()) {
                  player.sendMessage(config.getString("messages.gift-cooldown").replace("{1}", String.valueOf((cooldown.get(player) - System.currentTimeMillis()) / 1000L)));
                  return;
                }
                player.sendMessage(config.getString("messages.gift-give").replace("{1}", player2.getName()));
                player2.sendMessage(config.getString("messages.gift-get").replace("{1}", player.getName()));
                if (item.getStringNBT("hasFlagPersival") == "no")
                  item.removeFlag(ItemFlag.HIDE_ATTRIBUTES);
                item.removeNBT("persiId");
                item.removeNBT("isConduit");
                item.removeNBT("PersiItem");
                item.removeNBT("hasFlagPersival");
                item.removeNBT("hasLorePersival");
                item = removeLore(item);
                if(player2.getInventory().firstEmpty() == -1) {
                  player2.sendMessage(config.getString("messages.gift-get-not-place"));
                  player2.getWorld().dropItem(player2.getLocation(), item);
                } else
                  player2.getInventory().addItem(item);
                player.getOpenInventory().setItem(37, getConduit(player, player2));
                if(cooldown.containsKey(player))
                  cooldown.replace(player, System.currentTimeMillis() + (this.config.getInt("items.gift.cooldown") * 1000L));
                else
                  cooldown.put(player, System.currentTimeMillis() + (this.config.getInt("items.gift.cooldown") * 1000L));
              } else {
                e.setCancelled(true);
                if (item.getStringNBT("hasFlagPersival") == "no")
                  item.removeFlag(ItemFlag.HIDE_ATTRIBUTES);
                item.removeNBT("persiId");
                item.removeNBT("isConduit");
                item.removeNBT("PersiItem");
                item.removeNBT("hasFlagPersival");
                item.removeNBT("hasLorePersival");
                item = removeLore(item);
                if(player.getInventory().firstEmpty() == -1)
                  player.getWorld().dropItem(player2.getLocation(), item);
                else
                  player.getInventory().addItem(item);
                player.getOpenInventory().setItem(37, getConduit(player, player2));
              }
            }
            return;
          default:
            e.setCancelled(true);
            player.getInventory().setItem(e.getSlot(), new ItemStack(Material.AIR));
            item.setStringNBT("persiId", "conduit");
            item.setStringNBT("isConduit", "no");
            if(item.hasFlag(ItemFlag.HIDE_ATTRIBUTES))
              item.setStringNBT("hasFlagPersival", "yes");
            else {
              item.addFlag(ItemFlag.HIDE_ATTRIBUTES);
              item.setStringNBT("hasFlagPersival", "no");
            }
            if(item.getLore() == null)
              item.setStringNBT("hasLorePersival", "no");
            else
              item.setStringNBT("hasLorePersival", "yes");
            item = addLore(item);
            player.getOpenInventory().setItem(37, item);
            return;
          case "blank":
            e.setCancelled(true);
        }
      }
  }

  private ItemPersi addLore(ItemPersi item) {
    List<String> lore = item.getLore();
    if(lore == null)
      lore = new ArrayList<>();
    List<String> bonusLore = config.getStringList("items.gift.lore-item");
    for (String line : bonusLore)
      lore.add(line);
    item.setLore(lore, true, false, null);
    return item;
  }
  private ItemPersi removeLore(ItemPersi item) {
    List<String> lore = item.getLore();
    List<String> bonusLore = config.getStringList("items.gift.lore-item");
    for (String ignored : bonusLore)
      lore.remove(lore.size() - 1);
    item.setLore(lore, false, false, null);
    return item;
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

  private ItemStack getConduit(Player player1, Player player2) {

    ItemPersi conduit = new ItemPersi(Material.CONDUIT, 1);
    conduit.setStringNBT("persiId", "conduit");
    conduit.setStringNBT("isConduit", "yes");
    conduit.setStringNBT("blocked", "yes");
    conduit.setStringNBT("p1", player1.getUniqueId().toString());
    conduit.setStringNBT("p2", player2.getUniqueId().toString());
    conduit.setName(config.getString("items.gift.name"), true, true, player2);
    conduit.setLore(config.getStringList("items.gift.lore"), true, true, player2);

    return conduit;
  }
}
