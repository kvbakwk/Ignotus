package me.thekuba.handlers;

import java.util.ArrayList;
import java.util.List;

import me.thekuba.Ignotus;
import me.thekuba.items.ItemPersi;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ClearHandler {
  private int x;
  
  private final Ignotus pluginP = (Ignotus) Bukkit.getServer().getPluginManager().getPlugin("Ignotus");
  
  private final FileConfiguration config;
  
  private List<ItemStack> items = new ArrayList<>();
  
  private BossBar bar = Bukkit.createBossBar(ChatColor.translateAlternateColorCodes('&', ""), BarColor.BLUE, BarStyle.SEGMENTED_20, new org.bukkit.boss.BarFlag[0]);
  
  public ClearHandler(Plugin plugin) {
    this.config = plugin.getConfig();
    List<World> worlds = Bukkit.getServer().getWorlds();
    aLoop(plugin, worlds);
  }
  
  public void aLoop(final Plugin plugin, final List<World> w) {
    this.x = 0;
    Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
          public void run() {
            List<Integer> array = ClearHandler.this.config.getIntegerList("abyss.timer");
            int interval = ClearHandler.this.config.getInt("abyss.interval");
            ClearHandler.this.x++;
            if (ClearHandler.this.x == interval * 20) {
              ClearHandler.this.items = ClearHandler.this.clearWorlds(w);
              if (!ClearHandler.this.config.getString("messages.abyss-open").equals(""))
                Bukkit.broadcastMessage(ClearHandler.this.config.getString("messages.abyss-open").replace("{1}", Integer.toString(ClearHandler.this.items.size()))); 
              ClearHandler.this.pluginP.applyAbyss(ClearHandler.this.items);
              for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getOpenInventory() != null) {
                  ItemPersi item = new ItemPersi(p.getOpenInventory().getItem(0));
                  if (item != null && item.getType() != Material.AIR && item.getStringNBT("inventory") == "abyssPersival")
                    p.closeInventory(); 
                } 
              } 
            } 
            if (ClearHandler.this.x == interval * 20 - ClearHandler.this.config.getInt("abyss.bossbar.time") * 20 && !ClearHandler.this.config.getString("abyss.bossbar.before").equals("") && !ClearHandler.this.config.getString("abyss.bossbar.after").equals("")) {
              for (Player player : Bukkit.getServer().getOnlinePlayers())
                ClearHandler.this.bar.addPlayer(player); 
              ClearHandler.this.bar.setVisible(true);
              Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                int y = 0;

                int count = -1;

                double progress = 1.0D;

                double time = 1.0D / (ClearHandler.this.config.getInt("abyss.bossbar.time") * 20);

                public void run() {
                  ClearHandler.this.bar.setProgress(this.progress);
                  switch (this.count) {
                    case -1:
                      ClearHandler.this.bar.setTitle(ClearHandler.this.config.getString("abyss.bossbar.before"));
                      ClearHandler.this.bar.setColor(BarColor.BLUE);
                      break;
                    default:
                      ClearHandler.this.bar.setTitle(ClearHandler.this.config.getString("abyss.bossbar.after").replace("{1}", Integer.toString(ClearHandler.this.items.size())));
                      ClearHandler.this.bar.setColor(BarColor.BLUE);
                      break;
                  }
                  this.progress -= this.time;
                  if (this.progress <= 0.0D) {
                    this.count++;
                    this.progress = 1.0D;
                  }
                  if (this.y == ClearHandler.this.config.getInt("abyss.bossbar.time") * 40) {
                    ClearHandler.this.bar.setVisible(false);
                    this.count = -1;
                  }
                  this.y++;
                }
              },0L, 1L);
            } 
            if (ClearHandler.this.x == interval * 20 - 60 && 
              !ClearHandler.this.config.getString("messages.abyss-last3msg").equals(""))
              Bukkit.broadcastMessage(ClearHandler.this.config.getString("messages.abyss-last3msg").replace("{1}", "3")); 
            if (ClearHandler.this.x == interval * 20 - 40 && 
              !ClearHandler.this.config.getString("messages.abyss-last3msg").equals(""))
              Bukkit.broadcastMessage(ClearHandler.this.config.getString("messages.abyss-last3msg").replace("{1}", "2")); 
            if (ClearHandler.this.x == interval * 20 - 20 && 
              !ClearHandler.this.config.getString("messages.abyss-last3msg").equals(""))
              Bukkit.broadcastMessage(ClearHandler.this.config.getString("messages.abyss-last3msg").replace("{1}", "1")); 
            for (int i = 0; i < (array.toArray()).length; i++) {
              if (ClearHandler.this.x == interval * 20 - ((Integer)array.get(i)).intValue() * 20 && 
                !ClearHandler.this.config.getString("messages.abyss-timer").equals(""))
                Bukkit.broadcastMessage(ClearHandler.this.config.getString("messages.abyss-timer").replace("{1}", Integer.toString(((Integer)array.get(i)).intValue()))); 
            } 
            if (ClearHandler.this.x == ClearHandler.this.config.getInt("abyss.lookable") * 20 && 
              ClearHandler.this.config.getInt("abyss.lookable") > 0)
              Bukkit.broadcastMessage(ClearHandler.this.config.getString("messages.abyss-isclosing")); 
            if (ClearHandler.this.x == interval * 20)
              ClearHandler.this.x = 0; 
          }
        },0L, 1L);
  }
  
  private List<ItemStack> clearWorlds(List<World> worlds) {
    ArrayList<ItemStack> itemAll = new ArrayList<>();
    for (World world : worlds) {
      for (Entity entity : world.getEntities()) {
        if (entity.getType() == EntityType.DROPPED_ITEM) {
          Item item = (Item)entity;
          itemAll.add(item.getItemStack());
          entity.remove();
        } 
      } 
    } 
    return itemAll;
  }
  
  public int getTime() {
    return this.config.getInt("abyss.interval") - this.x / 20;
  }
  
  public List<ItemStack> getItems() {
    return this.items;
  }
}
