package me.thekuba.managers;

import java.util.ArrayList;
import java.util.List;

import me.thekuba.Ignotus;
import me.thekuba.IgnotusItem;
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

public class AbyssManager {
  private final Ignotus plugin;
  private final FileConfiguration config, messagesConfig;

  private int x, y, count, interval, size;
  private double progress, time;
  private List<Integer> array;
  private List<ItemStack> items;
  private final BossBar bar = Bukkit.createBossBar("", BarColor.BLUE, BarStyle.SEGMENTED_20);


  public AbyssManager(Ignotus plugin) {
    this.plugin = plugin;
    this.config = plugin.getConfig();
    this.messagesConfig = plugin.messagesFile.getConfig();
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
    this.bar.setVisible(false);

    Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {

      x++;

      // Abyss working
      {
        if (x == interval * 20) {
          this.items = clearWorlds(w);
          this.size = items.size();
          this.plugin.applyAbyss(items);
          for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getOpenInventory() != null) {
              IgnotusItem item = new IgnotusItem(p.getOpenInventory().getItem(0));
              if (item != null && item.getType() != Material.AIR && item.getStringNBT("inventory").equals("abyssIgnotus"))
                p.closeInventory();
            }
          }
          x = 0;
        }
      }
      // Chat messages
      {
              if (x == 0) {
                if (!messagesConfig.getString("abyss.open").equals(""))
                  Bukkit.broadcastMessage(this.plugin.colorCodes(messagesConfig.getString("abyss.open")
                          .replace("{1}", Integer.toString(size))));
              }
              for (int i = 0; i < (array.toArray()).length; i++) {
                if (x == interval * 20 - array.get(i) * 20 &&
                        !messagesConfig.getString("abyss.timer").equals(""))
                  Bukkit.broadcastMessage(this.plugin.colorCodes(messagesConfig.getString("abyss.timer")
                          .replace("{1}", Integer.toString(array.get(i)))));
              }
              if (x == interval * 20 - 60
                      && !messagesConfig.getString("abyss.last3msg").equals(""))
                Bukkit.broadcastMessage(this.plugin.colorCodes(messagesConfig.getString("abyss.last3msg")
                        .replace("{1}", "3")));
              if (x == interval * 20 - 40
                      && !messagesConfig.getString("abyss.last3msg").equals(""))
                Bukkit.broadcastMessage(this.plugin.colorCodes(messagesConfig.getString("abyss.last3msg")
                        .replace("{1}", "2")));
              if (x == interval * 20 - 20
                      && !messagesConfig.getString("abyss.last3msg").equals(""))
                Bukkit.broadcastMessage(this.plugin.colorCodes(messagesConfig.getString("abyss.last3msg")
                        .replace("{1}", "1")));
              if (x == config.getInt("abyss.lookable") * 20 && config.getInt("abyss.lookable") > 0)
                Bukkit.broadcastMessage(this.plugin.colorCodes(messagesConfig.getString("abyss.is-closing")));
      }
      // BossBar activity
      {
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
        if ((x >= interval * 20 - config.getInt("abyss.bossbar.time") * 20 || y > 0)
                && !config.getString("abyss.bossbar.before").equals("")
                && !config.getString("abyss.bossbar.after").equals("")) {
          bar.setProgress(progress);

          if (count == -1)
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
      }
    },0L, 1L);
  }
  private List<ItemStack> clearWorlds(List<World> worlds) {
    ArrayList<ItemStack> allItems = new ArrayList<>();
    for (World world : worlds) {
      for (Entity entity : world.getEntities()) {
        if (entity.getType() == EntityType.DROPPED_ITEM) {
          Item item = (Item)entity;
          allItems.add(item.getItemStack());
          entity.remove();
        } 
      } 
    }
    return allItems;
  }


  public int getTime() {
    return this.config.getInt("abyss.interval") - this.x / 20;
  }
  public void addPlayerToBar(Player player) {
      this.bar.addPlayer(player);
  }
  public List<ItemStack> getItems() {
    return this.items;
  }
}
