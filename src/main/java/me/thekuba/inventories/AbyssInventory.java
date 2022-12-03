package me.thekuba.inventories;

import java.util.ArrayList;
import java.util.List;

import me.thekuba.Ignotus;
import me.thekuba.items.ItemPersi;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

public class AbyssInventory {
  public Inventory inv;
  
  private static final FileConfiguration config = ((Ignotus)Ignotus.getPlugin(Ignotus.class)).getConfig();
  
  public AbyssInventory(List<ItemStack> items, int option, int page) {
    newInv(items, option, page);
  }
  
  public void newInv(List<ItemStack> items, int option, int page) {
    String title = ChatColor.translateAlternateColorCodes('&', config.getString("abyss.title"));
    this.inv = Bukkit.createInventory(null, 54, title);
    ItemStack[] itemsInv = new ItemStack[54];
    List<ItemStack> itemsCustom = getItems(page);
    ItemStack arrowRight = itemsCustom.get(0);
    ItemStack arrowLeft = itemsCustom.get(1);
    ItemStack blank = itemsCustom.get(2);
    for (int i = 0; i < 54; i++) {
      if ((i < 9 || i > 44) && itemsInv[i] == null) {
        itemsInv[i] = blank;
      } else {
        itemsInv[i] = null;
      } 
      this.inv.setItem(i, itemsInv[i]);
    } 
    List<ItemStack> itemsAll = items;
    if (itemsAll.size() == 0)
      return; 
    int amountTimes = itemsAll.size();
    for (int j = 0; j < 36 - amountTimes; j++)
      itemsAll.add(new ItemStack(Material.AIR)); 
    int licznik = 0;
    for (int k = 0; k < 54; k++) {
      if (this.inv.getItem(k) == null) {
        this.inv.setItem(k, itemsAll.get(licznik));
        licznik++;
      } 
      if (option == 0) {
        this.inv.setItem(51, arrowRight);
      } else if (option == 1) {
        this.inv.setItem(47, arrowLeft);
      } else if (option == 2) {
        this.inv.setItem(47, arrowLeft);
        this.inv.setItem(51, arrowRight);
      } 
    } 
  }
  
  private static List<ItemStack> getItems(int page) {
    ItemPersi arrowRight = new ItemPersi(Material.TIPPED_ARROW);
    arrowRight.setName(config.getString("abyss.arrow-right.name"), true, false, null);
    arrowRight.setLore(config.getStringList("abyss.arrow-right.lore"), true, false, null);
    arrowRight.addFlag(ItemFlag.HIDE_POTION_EFFECTS);
    arrowRight.setPotion(PotionType.LUCK);
    arrowRight.setStringNBT("persiId", "arrowRight");
    arrowRight.setStringNBT("blocked", "yes");
    ItemPersi arrowLeft = new ItemPersi(Material.TIPPED_ARROW);
    arrowLeft.setName(config.getString("abyss.arrow-left.name"), true, false, null);
    arrowLeft.setLore(config.getStringList("abyss.arrow-left.lore"), true, false, null);
    arrowLeft.addFlag(ItemFlag.HIDE_POTION_EFFECTS);
    arrowLeft.setPotion(PotionType.STRENGTH);
    arrowLeft.setStringNBT("persiId", "arrowLeft");
    arrowLeft.setStringNBT("blocked", "yes");
    ItemPersi blank = ItemPersi.Blank(Material.valueOf(config.getString("items.blank.material").toUpperCase()));
    blank.setStringNBT("inventory", "abyssPersival");
    blank.setIntNBT("pagePersival", page);
    List<ItemStack> items = new ArrayList<>();
    items.add(arrowRight);
    items.add(arrowLeft);
    items.add(blank);
    return items;
  }
}
