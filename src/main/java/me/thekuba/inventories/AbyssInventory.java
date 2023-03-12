package me.thekuba.inventories;

import java.util.ArrayList;
import java.util.List;

import me.thekuba.Ignotus;
import me.thekuba.IgnotusItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionType;

public class AbyssInventory {
  private final static Plugin plugin = Ignotus.getPlugin(Ignotus.class);
  private final static FileConfiguration config = plugin.getConfig();

  public Inventory inv;

  
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

    if (items.size() == 0)
      return; 
    int amountTimes = items.size();
    for (int j = 0; j < 36 - amountTimes; j++)
      items.add(new ItemStack(Material.AIR));
    int counter = 0;
    for (int k = 0; k < 54; k++) {
      if (this.inv.getItem(k) == null) {
        this.inv.setItem(k, items.get(counter));
        counter++;
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
  
  private List<ItemStack> getItems(int page) {
    IgnotusItem arrowRight = new IgnotusItem(Material.TIPPED_ARROW);
    arrowRight.setName(config.getString("abyss.arrow-right.name"), true, false, null);
    arrowRight.setLore(config.getStringList("abyss.arrow-right.lore"), true, false, null);
    arrowRight.addFlag(ItemFlag.HIDE_POTION_EFFECTS);
    arrowRight.setPotion(PotionType.LUCK);
    arrowRight.setStringNBT("persiId", "arrowRight");
    arrowRight.setStringNBT("blocked", "yes");

    IgnotusItem arrowLeft = new IgnotusItem(Material.TIPPED_ARROW);
    arrowLeft.setName(config.getString("abyss.arrow-left.name"), true, false, null);
    arrowLeft.setLore(config.getStringList("abyss.arrow-left.lore"), true, false, null);
    arrowLeft.addFlag(ItemFlag.HIDE_POTION_EFFECTS);
    arrowLeft.setPotion(PotionType.STRENGTH);
    arrowLeft.setStringNBT("persiId", "arrowLeft");
    arrowLeft.setStringNBT("blocked", "yes");

    IgnotusItem blank = IgnotusItem.Blank(Material.valueOf(config.getString("items.blank.material").toUpperCase()));
    blank.setStringNBT("inventory", "abyssPersival");
    blank.setIntNBT("pagePersival", page);

    List<ItemStack> items = new ArrayList<>();
    items.add(arrowRight);
    items.add(arrowLeft);
    items.add(blank);
    return items;
  }
}
