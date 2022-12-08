package me.thekuba.handlers;

import java.util.ArrayList;
import java.util.List;

import me.thekuba.Ignotus;
import me.thekuba.items.ItemPersi;
import org.bukkit.Bukkit;
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
  private final Ignotus plugin;
  private final FileConfiguration config;

  private int x, y, count, interval, size;
  private double progress, time;
  private List<ItemStack> items = new ArrayList<>();
  private List<Integer> array;
  
  private final BossBar bar = Bukkit.createBossBar("", BarColor.BLUE, BarStyle.SEGMENTED_20);
  
  public ClearHandler(Ignotus plugin) {
    this.plugin = plugin;
    this.config = plugin.getConfig();
    List<World> worlds = Bukkit.getServer().getWorlds();
    aLoop(plugin, worlds);
  }
  
  public void aLoop(final Plugin plugin, final List<World> w) {

    this.x = 0;
    this.y = 0;
    this.size = 0;
    this.count = -1;
    this.progress = 1.0D;
    this.time = 1.0D / (config.getInt("abyss.bossbar.time") * 20);
    this.interval = config.getInt("abyss.interval");
    this.array = config.getIntegerList("abyss.timer");

    Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {

      x++;

      if (x == interval * 20) {
        items = clearWorlds(w);
        size = items.size();

        if (!(config.getString("messages.abyss-open").equals("")))
          Bukkit.broadcastMessage(config.getString("messages.abyss-open")
                  .replace("{1}", Integer.toString(size)));

        this.plugin.applyAbyss(items);

        for (Player p : Bukkit.getOnlinePlayers()) {
          if (p.getOpenInventory() != null) {
            ItemPersi item = new ItemPersi(p.getOpenInventory().getItem(0));
            if (item != null && item.getType() != Material.AIR && item.getStringNBT("inventory").equals("abyssPersival"))
              p.closeInventory();
          }
        }

        x = 0;
      }

      if (x == interval * 20 - config.getInt("abyss.bossbar.time") * 20
              && !config.getString("abyss.bossbar.before").equals("")
              && !config.getString("abyss.bossbar.after").equals("")) {
        for (Player player : Bukkit.getServer().getOnlinePlayers())
          bar.addPlayer(player);

        bar.setProgress(1);
        bar.setVisible(true);

        count = -1;
        y = 0;
      }

      if((x >= interval * 20 - config.getInt("abyss.bossbar.time") * 20 || y > 0)
              && !config.getString("abyss.bossbar.before").equals("")
              && !config.getString("abyss.bossbar.after").equals("")) {
        bar.setProgress(progress);

        if(count == -1)
            bar.setTitle(config.getString("abyss.bossbar.before"));
        else
            bar.setTitle(config.getString("abyss.bossbar.after")
                    .replace("{1}", Integer.toString(size)));
        bar.setColor(BarColor.BLUE);

        progress -= time;

        if (progress <= 0.0D) {
          count++;
          progress = 1.0D;
        }

        y++;

        if (y == config.getInt("abyss.bossbar.time") * 40) {
          bar.setVisible(false);
          count = -1;
          y = 0;
        }
      }

      if (x == interval * 20 - 60
              && !config.getString("messages.abyss-last3msg").equals(""))
        Bukkit.broadcastMessage(config.getString("messages.abyss-last3msg").replace("{1}", "3"));
      if (x == interval * 20 - 40
              && !config.getString("messages.abyss-last3msg").equals(""))
        Bukkit.broadcastMessage(config.getString("messages.abyss-last3msg").replace("{1}", "2"));
      if (x == interval * 20 - 20
              && !config.getString("messages.abyss-last3msg").equals(""))
        Bukkit.broadcastMessage(config.getString("messages.abyss-last3msg").replace("{1}", "1"));

      for (int i = 0; i < (array.toArray()).length; i++) {
        if (x == interval * 20 - array.get(i) * 20 &&
          !config.getString("messages.abyss-timer").equals(""))
          Bukkit.broadcastMessage(config.getString("messages.abyss-timer").replace("{1}", Integer.toString(array.get(i))));
      }

      if (x == config.getInt("abyss.lookable") * 20 && config.getInt("abyss.lookable") > 0)
        Bukkit.broadcastMessage(config.getString("messages.abyss-isclosing"));


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



  public void addPlayerToBar(Player player) {
    this.bar.addPlayer(player);
  }



  public int getTime() {
    return this.config.getInt("abyss.interval") - this.x / 20;
  }
  public List<ItemStack> getItems() {
    return this.items;
  }
}
