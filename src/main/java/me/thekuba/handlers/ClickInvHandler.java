package me.thekuba.handlers;

import java.util.*;

import me.thekuba.Ignotus;
import me.thekuba.IgnotusItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class ClickInvHandler implements Listener {
  private final Ignotus plugin;
  private final FileConfiguration config, messagesConfig;

  private final Map<Player, Long> cooldown = new HashMap<>();

  public ClickInvHandler(Ignotus plugin) {
      this.plugin = plugin;
      this.config = plugin.getConfig();
      this.messagesConfig = plugin.messagesFile.getConfig();
      Bukkit.getPluginManager().registerEvents(this, plugin);
  }
  
  @EventHandler
  public void onClickInventory(InventoryClickEvent e) {
      if(!checkItem(e.getWhoClicked().getOpenInventory().getItem(0)))
        return;
      if(!checkItem(e.getCurrentItem()))
        return;

      Player player = (Player) e.getWhoClicked();

      // Abyss Inventory Click
      if(new IgnotusItem(e.getWhoClicked().getOpenInventory().getItem(0)).getStringNBT("inventory").equals("abyssIgnotus")) {
        IgnotusItem item = new IgnotusItem(e.getCurrentItem());
        switch (item.getStringNBT("ignotusId")) {
          case "arrowLeft":
            e.setCancelled(true);
            e.getWhoClicked().openInventory(plugin.abyssInv.get(new IgnotusItem(e.getWhoClicked().getOpenInventory().getItem(0)).getIntNBT("pageIgnotus") - 1));
            return;
          case "arrowRight":
            e.setCancelled(true);
            e.getWhoClicked().openInventory(plugin.abyssInv.get(new IgnotusItem(e.getWhoClicked().getOpenInventory().getItem(0)).getIntNBT("pageIgnotus") + 1));
            return;
          case "blank":
            e.setCancelled(true);
            System.out.println("sss");
            return;
          default:
            if(e.getWhoClicked().getInventory().equals(e.getClickedInventory()))
              e.setCancelled(true);
        }
      }

      // Interact Inventory Click
      else if(new IgnotusItem(e.getWhoClicked().getOpenInventory().getItem(0)).getStringNBT("inventory").equals("interactIgnotus")) {
        IgnotusItem item = new IgnotusItem(e.getCurrentItem());
        Player player2 = Bukkit.getPlayer(UUID.fromString(new IgnotusItem(e.getWhoClicked().getOpenInventory().getItem(0)).getStringNBT("p2")));

        switch (item.getStringNBT("ignotusId")) {
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
                  player.sendMessage(plugin.colorCodes(messagesConfig.getString("gift.cooldown")
                          .replace("{1}", String.valueOf((cooldown.get(player) - System.currentTimeMillis()) / 1000L))));
                  return;
                }
                player.sendMessage(plugin.colorCodes(messagesConfig.getString("gift.give")
                        .replace("{1}", player2.getName())));
                player2.sendMessage(plugin.colorCodes(messagesConfig.getString("gift.get")
                        .replace("{1}", player.getName())));
                if (item.getStringNBT("hasFlagPersival").equals("no"))
                  item.removeFlag(ItemFlag.HIDE_ATTRIBUTES);
                item.removeNBT("ignotusId");
                item.removeNBT("isConduit");
                item.removeNBT("PersiItem");
                item.removeNBT("hasFlagPersival");
                item.removeNBT("hasLorePersival");
                item = removeLore(item);
                if(player2.getInventory().firstEmpty() == -1) {
                  player2.sendMessage(plugin.colorCodes(messagesConfig.getString("gift.get-not-place")));
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
                if (item.getStringNBT("hasFlagPersival").equals("no"))
                  item.removeFlag(ItemFlag.HIDE_ATTRIBUTES);
                item.removeNBT("ignotusId");
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
            item.setStringNBT("ignotusId", "conduit");
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


  private IgnotusItem addLore(IgnotusItem item) {
    List<String> lore;
    if(item.getLore() == null)
      lore = new ArrayList<>();
    else
      lore = item.getLore();
    lore.addAll(config.getStringList("items.gift.lore-item"));
    item.setLore(lore, true, false, null);
    return item;
  }
  private IgnotusItem removeLore(IgnotusItem item) {
    List<String> lore = item.getLore();
    for(int i = 0; i < config.getStringList("items.gift.lore-item").size(); i++) {
      lore.remove(lore.size() - 1);
    }
    item.setLore(lore, false, false, null);
    return item;
  }
  private ItemStack getConduit(Player player1, Player player2) {
    IgnotusItem conduit = new IgnotusItem(Material.CONDUIT, 1);
    conduit.setStringNBT("ignotusId", "conduit");
    conduit.setStringNBT("isConduit", "yes");
    conduit.setStringNBT("blocked", "yes");
    conduit.setStringNBT("p1", player1.getUniqueId().toString());
    conduit.setStringNBT("p2", player2.getUniqueId().toString());
    conduit.setName(config.getString("items.gift.name"), true, true, player2);
    conduit.setLore(config.getStringList("items.gift.lore"), true, true, player2);
    return conduit;
  }
  private boolean checkItem(ItemStack item) {
    return item != null && item.getType() != Material.AIR && new IgnotusItem(item).getStringNBT("inventory") != null;
  }

}
