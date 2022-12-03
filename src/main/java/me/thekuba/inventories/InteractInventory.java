package me.thekuba.inventories;

import java.util.ArrayList;
import java.util.List;

import de.tr7zw.nbtapi.NBTItem;
import me.clip.placeholderapi.PlaceholderAPI;
import me.thekuba.Ignotus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import me.thekuba.Ignotus;
import me.thekuba.items.ItemPersi;

public class InteractInventory {
  private static Inventory inv;
  
  private static Player player1;
  
  private static Player player2;
  
  static Plugin plugin = (Plugin) Ignotus.getPlugin(Ignotus.class);
  
  static FileConfiguration config = plugin.getConfig();
  
  public static void openInv(Player p1, Player p2) {
    String title = ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(p2, config.getString("items.title")));
    StringBuilder titleB = new StringBuilder();
    for (int i = 0; i < 21 - title.length() / 2; i++)
      titleB.append(" "); 
    titleB.append(title);
    title = titleB.toString();
    inv = Bukkit.createInventory(null, 54, title);
    player1 = p1;
    player2 = p2;
    ItemStack[] itemsInv = new ItemStack[54];
    List<ItemStack> itemsCustom = getItems();
    ItemStack profil = itemsCustom.get(0);
    ItemStack item0 = itemsCustom.get(1);
    ItemStack item1 = itemsCustom.get(2);
    ItemStack item2 = itemsCustom.get(3);
    ItemStack item3 = itemsCustom.get(4);
    ItemStack item4 = itemsCustom.get(5);
    ItemStack itemMainHand = itemsCustom.get(6);
    ItemStack itemOffHand = itemsCustom.get(7);
    ItemStack itemHead = itemsCustom.get(8);
    ItemStack itemChestplate = itemsCustom.get(9);
    ItemStack itemLeggings = itemsCustom.get(10);
    ItemStack itemBoots = itemsCustom.get(11);
    ItemStack blankBlack = itemsCustom.get(12);
    itemsInv[10] = profil;
    itemsInv[12] = item0;
    itemsInv[28] = item1;
    itemsInv[29] = item2;
    itemsInv[37] = item3;
    itemsInv[38] = item4;
    itemsInv[23] = itemMainHand;
    itemsInv[25] = itemOffHand;
    itemsInv[15] = itemHead;
    itemsInv[24] = itemChestplate;
    itemsInv[33] = itemLeggings;
    itemsInv[42] = itemBoots;
    int j;
    for (j = 0; j < 54; j++) {
      if (itemsInv[j] == null)
        itemsInv[j] = blankBlack; 
    } 
    for (j = 0; j < 54; j++)
      inv.setItem(j, itemsInv[j]); 
    p1.openInventory(inv);
  }
  
  private static List<ItemStack> getItems() {
    ItemPersi profil = new ItemPersi(Material.PLAYER_HEAD, 1);
    profil.setSkull(player2);
    profil.setStringNBT("persiId", "blank");
    profil.setStringNBT("blocked", "yes");
    profil.setStringNBT("inventory", "interPersival");
    profil.setStringNBT("p1", player1.getUniqueId().toString());
    profil.setStringNBT("p2", player2.getUniqueId().toString());
    profil.setName(config.getString("items.profile.name"), true, true, player2);
    profil.setLore(config.getStringList("items.profile.lore"), true, true, player2);
    ItemPersi item0 = new ItemPersi(Material.REPEATER, 1);
    item0.setStringNBT("persiId", "i0");
    item0.setStringNBT("blocked", "yes");
    item0.setStringNBT("p1", player1.getUniqueId().toString());
    item0.setStringNBT("p2", player2.getUniqueId().toString());
    item0.setName(config.getString("items.item0.name"), true, true, player2);
    item0.setLore(config.getStringList("items.item0.lore"), true, true, player2);
    ItemPersi item1 = new ItemPersi(Material.ENDER_EYE, 1);
    item1.setStringNBT("persiId", "i1");
    item1.setStringNBT("blocked", "yes");
    item1.setStringNBT("p1", player1.getUniqueId().toString());
    item1.setStringNBT("p2", player2.getUniqueId().toString());
    item1.setName(config.getString("items.item1.name"), true, true, player2);
    item1.setLore(config.getStringList("items.item1.lore"), true, true, player2);
    ItemPersi item2 = new ItemPersi(Material.BRICKS, 1);
    item2.setStringNBT("persiId", "i2");
    item2.setStringNBT("blocked", "yes");
    item2.setStringNBT("p1", player1.getUniqueId().toString());
    item2.setStringNBT("p2", player2.getUniqueId().toString());
    item2.setName(config.getString("items.item2.name"), true, true, player2);
    item2.setLore(config.getStringList("items.item2.lore"), true, true, player2);
    ItemPersi item3 = new ItemPersi(Material.CONDUIT, 1);
    item3.setStringNBT("persiId", "blank");
    item3.setStringNBT("persivalConduit", "yes");
    item3.setStringNBT("blocked", "yes");
    item3.setStringNBT("p1", player1.getUniqueId().toString());
    item3.setStringNBT("p2", player2.getUniqueId().toString());
    item3.setName(config.getString("items.gift.name"), true, true, player2);
    item3.setLore(config.getStringList("items.gift.lore"), true, true, player2);
    ItemPersi item4 = new ItemPersi(Material.CLOCK, 1);
    item4.setStringNBT("persiId", "blank");
    item4.setStringNBT("blocked", "yes");
    item4.setStringNBT("p1", player1.getUniqueId().toString());
    item4.setStringNBT("p2", player2.getUniqueId().toString());
    item4.setName(config.getString("items.media.name"), true, true, player2);
    item4.setLore(config.getStringList("items.media.lore"), true, true, player2);
    ItemStack blank = ItemPersi.Blank(Material.BLACK_STAINED_GLASS_PANE);
    ItemStack temp = player2.getInventory().getItemInMainHand();
    if (temp == null || temp.getType() == Material.AIR)
      temp = blank; 
    ItemStack itemMainHand = new ItemStack(temp.getType(), temp.getAmount());
    itemMainHand.setItemMeta(temp.getItemMeta());
    NBTItem nbti = new NBTItem(itemMainHand);
    nbti.setString("persiId", "blank");
    nbti.setString("blocked", "yes");
    nbti.setString("p1", player1.getUniqueId().toString());
    nbti.setString("p2", player2.getUniqueId().toString());
    nbti.applyNBT(itemMainHand);
    temp = player2.getInventory().getItemInOffHand();
    if (temp == null || temp.getType() == Material.AIR)
      temp = blank; 
    ItemStack itemOffHand = new ItemStack(temp.getType(), temp.getAmount());
    itemOffHand.setItemMeta(temp.getItemMeta());
    nbti = new NBTItem(itemOffHand);
    nbti.setString("persiId", "blank");
    nbti.setString("blocked", "yes");
    nbti.setString("p1", player1.getUniqueId().toString());
    nbti.setString("p2", player2.getUniqueId().toString());
    nbti.applyNBT(itemOffHand);
    temp = player2.getInventory().getHelmet();
    if (temp == null || temp.getType() == Material.AIR)
      temp = blank; 
    ItemStack itemHead = new ItemStack(temp.getType(), temp.getAmount());
    itemHead.setItemMeta(temp.getItemMeta());
    nbti = new NBTItem(itemHead);
    nbti.setString("persiId", "blank");
    nbti.setString("blocked", "yes");
    nbti.setString("p1", player1.getUniqueId().toString());
    nbti.setString("p2", player2.getUniqueId().toString());
    nbti.applyNBT(itemHead);
    temp = player2.getInventory().getChestplate();
    if (temp == null || temp.getType() == Material.AIR)
      temp = blank; 
    ItemStack itemChestplate = new ItemStack(temp.getType(), temp.getAmount());
    itemChestplate.setItemMeta(temp.getItemMeta());
    nbti = new NBTItem(itemChestplate);
    nbti.setString("persiId", "blank");
    nbti.setString("blocked", "yes");
    nbti.setString("p1", player1.getUniqueId().toString());
    nbti.setString("p2", player2.getUniqueId().toString());
    nbti.applyNBT(itemChestplate);
    temp = player2.getInventory().getLeggings();
    if (temp == null || temp.getType() == Material.AIR)
      temp = blank; 
    ItemStack itemLeggings = new ItemStack(temp.getType(), temp.getAmount());
    itemLeggings.setItemMeta(temp.getItemMeta());
    nbti = new NBTItem(itemLeggings);
    nbti.setString("persiId", "blank");
    nbti.setString("blocked", "yes");
    nbti.setString("p1", player1.getUniqueId().toString());
    nbti.setString("p2", player2.getUniqueId().toString());
    nbti.applyNBT(itemLeggings);
    temp = player2.getInventory().getBoots();
    if (temp == null || temp.getType() == Material.AIR)
      temp = blank; 
    ItemStack itemBoots = new ItemStack(temp.getType(), temp.getAmount());
    itemBoots.setItemMeta(temp.getItemMeta());
    nbti = new NBTItem(itemBoots);
    nbti.setString("persiId", "blank");
    nbti.setString("blocked", "yes");
    nbti.setString("p1", player1.getUniqueId().toString());
    nbti.setString("p2", player2.getUniqueId().toString());
    nbti.applyNBT(itemBoots);
    ItemStack blankBlack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
    nbti = new NBTItem(blankBlack);
    nbti.setString("persiId", "blank");
    nbti.setString("blocked", "yes");
    nbti.setString("p1", player1.getUniqueId().toString());
    nbti.setString("p2", player2.getUniqueId().toString());
    nbti.applyNBT(blankBlack);
    ItemMeta blankBlackM = blankBlack.getItemMeta();
    blankBlackM.setDisplayName(" ");
    blankBlack.setItemMeta(blankBlackM);
    List<ItemStack> items = new ArrayList<>();
    items.add(profil);
    items.add(item0);
    items.add(item1);
    items.add(item2);
    items.add(item3);
    items.add(item4);
    items.add(itemMainHand);
    items.add(itemOffHand);
    items.add(itemHead);
    items.add(itemChestplate);
    items.add(itemLeggings);
    items.add(itemBoots);
    items.add(blankBlack);
    return items;
  }
}
