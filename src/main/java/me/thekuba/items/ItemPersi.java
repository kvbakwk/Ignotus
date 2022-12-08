package me.thekuba.items;

import java.util.List;

import de.tr7zw.nbtapi.NBTItem;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class ItemPersi extends ItemStack {
  
  private boolean isNBT = false;
  private int amountNBT = 0;
  
  private final String NBTTitle = "PersiItem";
  
  public ItemPersi(Material material) {
    super(material);
  }
  
  public ItemPersi(Material material, int amount) {
    super(material, amount);
  }
  
  public ItemPersi(ItemStack item) {
    super(item);
  }
  
  public void setName(String name, boolean color, boolean placeholders, Player player) {
    if (placeholders)
      name = PlaceholderAPI.setPlaceholders(player, name);
    if (color)
      name = ChatColor.translateAlternateColorCodes('&', name); 
    ItemMeta itemM = getItemMeta();
    itemM.setDisplayName(name);
    setItemMeta(itemM);
  }
  
  public String getName() {
    return getItemMeta().getDisplayName();
  }
  
  public void setLore(List<String> lore, boolean color, boolean placeholders, Player player) {
    if (lore != null && lore.size() != 0) {
      int temp = 0;
      for (String loreLine : lore) {
        if (placeholders && color) {
          lore.set(temp, ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, loreLine)));
        } else if (placeholders) {
          lore.set(temp, PlaceholderAPI.setPlaceholders(player, loreLine));
        } else if (color) {
          lore.set(temp, ChatColor.translateAlternateColorCodes('&', loreLine));
        } 
        temp++;
      } 
    } else {
      lore = null;
    } 
    ItemMeta itemM = getItemMeta();
    itemM.setLore(lore);
    setItemMeta(itemM);
  }
  
  public List<String> getLore() {
    return getItemMeta().getLore();
  }
  
  public void setStringNBT(String key, String value) {
    NBTItem nbti = new NBTItem(this);
    if (!this.isNBT) {
      nbti.setBoolean("PersiItem", Boolean.valueOf(true));
      this.isNBT = true;
    } 
    nbti.setString(key, value);
    this.amountNBT++;
    nbti.applyNBT(this);
  }
  
  public void setIntNBT(String key, int value) {
    NBTItem nbti = new NBTItem(this);
    if (!this.isNBT) {
      nbti.setBoolean("PersiItem", Boolean.valueOf(true));
      this.isNBT = true;
    } 
    nbti.setInteger(key, Integer.valueOf(value));
    this.amountNBT++;
    nbti.applyNBT(this);
  }
  
  public void setBooleanNBT(String key, boolean value) {
    NBTItem nbti = new NBTItem(this);
    if (!this.isNBT) {
      nbti.setBoolean("PersiItem", Boolean.valueOf(true));
      this.isNBT = true;
    } 
    nbti.setBoolean(key, Boolean.valueOf(value));
    this.amountNBT++;
    nbti.applyNBT(this);
  }
  
  public String getStringNBT(String key) {
    String value = "unknown";
    NBTItem nbti = new NBTItem(this);
    value = nbti.getString(key);
    return value;
  }
  
  public int getIntNBT(String key) {
    int value = -1;
    NBTItem nbti = new NBTItem(this);
    value = nbti.getInteger(key).intValue();
    return value;
  }
  
  public boolean getBooleanNBT(String key) {
    boolean value = false;
    NBTItem nbti = new NBTItem(this);
    value = nbti.getBoolean(key).booleanValue();
    return value;
  }
  
  public boolean hasNBT(String key) {
    NBTItem nbti = new NBTItem(this);
    return nbti.hasKey(key).booleanValue();
  }
  
  public void removeNBT(String key) {
    NBTItem nbti = new NBTItem(this);
    nbti.removeKey(key);
    this.amountNBT--;
    if (this.amountNBT == 0)
      this.isNBT = false; 
    nbti.applyNBT(this);
  }
  
  public void setSkull(Player player) {
    OfflinePlayer playerO = Bukkit.getOfflinePlayer(player.getUniqueId());
    SkullMeta itemS = (SkullMeta)getItemMeta();
    itemS.setOwningPlayer(playerO);
    setItemMeta(itemS);
  }
  
  public void setPotion(PotionType type) {
    PotionMeta itemP = (PotionMeta)getItemMeta();
    itemP.setBasePotionData(new PotionData(type));
    setItemMeta(itemP);
  }
  
  public void addFlag(ItemFlag flag) {
    ItemMeta itemM = getItemMeta();
    itemM.addItemFlags(new ItemFlag[] { flag });
    setItemMeta(itemM);
  }
  
  public boolean hasFlag(ItemFlag flag) {
    return getItemMeta().hasItemFlag(flag);
  }
  
  public void removeFlag(ItemFlag flag) {
    ItemMeta itemM = getItemMeta();
    itemM.removeItemFlags(new ItemFlag[] { flag });
    setItemMeta(itemM);
  }
  
  public static ItemPersi Blank(Material material) {
    FileConfiguration config = Bukkit.getServer().getPluginManager().getPlugin("Ignotus").getConfig();
    String name = config.getString("items.blank.name");
    List<String> lore = config.getStringList("items.blank.lore");
    int amount = config.getInt("items.blank.amount");
    ItemPersi item = new ItemPersi(material, amount);
    item.setName(name, true, false, (Player)null);
    if (lore.size() != 0)
      item.setLore(lore, true, false, (Player)null); 
    item.setStringNBT("persiId", "blank");
    item.setStringNBT("blocked", "yes");
    return item;
  }
}
