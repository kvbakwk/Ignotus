package me.thekuba.handlers;

import java.util.HashMap;
import java.util.Map;

import de.myzelyam.api.vanish.VanishAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import me.thekuba.Ignotus;
import me.thekuba.files.GroupsManager;
import me.thekuba.files.PlayersManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinHandler implements Listener {
  private final Ignotus plugin;
  private final PlayersManager playersConfig;
  private final GroupsManager groupsConfig;

  private int x;
  private final Map<Player, ArmorStand[]> armorStands = new HashMap<>();

  
  public PlayerJoinHandler(@NotNull Ignotus plugin) {
    this.plugin = plugin;
    this.playersConfig = plugin.playersFile;
    this.groupsConfig = plugin.groupsFile;
    Bukkit.getPluginManager().registerEvents(this, plugin);
  }

  
  @EventHandler
  public void onPlayerJoin(@NotNull PlayerJoinEvent event) {

    final Player player = event.getPlayer();

    plugin.clear.addPlayerToBar(player);

    if (!this.playersConfig.getConfig().contains("players." + player.getUniqueId() + ".nick")) {
      this.playersConfig.getConfig().set("players." + player.getUniqueId() + ".nick", player.getName());
      this.playersConfig.getConfig().set("players." + player.getUniqueId() + ".instagram", "");
      this.playersConfig.getConfig().set("players." + player.getUniqueId() + ".youtube", "");
      this.playersConfig.getConfig().set("players." + player.getUniqueId() + ".twitch", "");
      this.playersConfig.getConfig().set("players." + player.getUniqueId() + ".discord", "");
      this.playersConfig.getConfig().set("players." + player.getUniqueId() + ".snapchat", "");
      this.playersConfig.getConfig().set("players." + player.getUniqueId() + ".status", "");
      this.playersConfig.saveConfig();
    }

    if (!this.plugin.getConfig().getBoolean("nametags.enable"))
      return;

    final Scoreboard score = Bukkit.getScoreboardManager().getMainScoreboard();

    this.setTeam(player, score);

    ArmorStand[] armorStand;
    final String name = getPrefix(player);
    if (!this.playersConfig.getConfig().getString("players." + player.getUniqueId() + ".status").equals("off"))
      armorStand = new ArmorStand[] { (ArmorStand)player.getWorld().spawnEntity(player.getLocation().add(0.0D, 1.75D, 0.0D), EntityType.ARMOR_STAND), (ArmorStand)player.getWorld().spawnEntity(player.getLocation().add(0.0D, 2.05D, 0.0D), EntityType.ARMOR_STAND) };
    else
      armorStand = new ArmorStand[] { (ArmorStand)player.getWorld().spawnEntity(player.getLocation().add(0.0D, 1.75D, 0.0D), EntityType.ARMOR_STAND), (ArmorStand)player.getWorld().spawnEntity(player.getLocation().add(0.0D, 1.75D, 0.0D), EntityType.ARMOR_STAND) };
    this.armorStands.put(player, armorStand);

    final String status = "§7" + this.playersConfig.getConfig().getString("players." + player.getUniqueId() + ".status");

    x = 0;
    Bukkit.getScheduler().runTaskTimer(this.plugin, () -> {

      if (x % 100 == 0)
        PlayerJoinHandler.this.setTeam(player, score);

      if (x == 0 && armorStands.get(player) != null)
        for (Player playerT : Bukkit.getServer().getOnlinePlayers()) {
          if (armorStands.get(playerT) != null)
            for (ArmorStand a : armorStands.get(playerT)) {
              a.setGravity(false);
              a.setCustomNameVisible(true);
              a.setSmall(true);
              a.setBasePlate(false);
              a.setVisible(false);
              a.setMarker(true);
              a.setCustomName(PlayerJoinHandler.this.getPrefix(playerT));
              a.setCustomName(PlayerJoinHandler.this.getStatus("§7" + playersConfig.getConfig().getString("players." + player.getUniqueId() + ".status"), playerT));
              a.teleport(playerT.getLocation().add(0.0D, 1.05D, 0.0D));
            }
        }

      if (armorStands.isEmpty() || armorStands.get(player) == null)
        return;

      if (armorStands.get(player)[1] != null && player.isOnline()) {
        if (!playersConfig.getConfig().getString("players." + player.getUniqueId() + ".status").equals("off")) {
          (armorStands.get(player))[0].setCustomNameVisible(true);
          if (!getPrefix(player).equals(name) || !playersConfig.getConfig().getString("players." + player.getUniqueId() + ".status").equals(status)) {
            (armorStands.get(player))[0].setCustomName(getStatus("§7" + playersConfig.getConfig().getString("players." + player.getUniqueId() + ".status"), player));
            (armorStands.get(player))[1].setCustomName(getPrefix(player));
          }
          if (player.getGameMode().equals(GameMode.SPECTATOR)) {
            (armorStands.get(player))[0].teleport(player.getLocation().add(0.0D, 50.0D, 0.0D));
            (armorStands.get(player))[1].teleport(player.getLocation().add(0.0D, 50.0D, 0.0D));
          } else if (player.isSwimming()) {
            (armorStands.get(player))[0].teleport(player.getLocation().add(0.0D, 0.3D, 0.0D));
            (armorStands.get(player))[1].teleport(player.getLocation().add(0.0D, 0.6D, 0.0D));
          } else if (player.isSneaking()) {
            (armorStands.get(player))[0].teleport(player.getLocation().add(0.0D, 1.45D, 0.0D));
            (armorStands.get(player))[1].teleport(player.getLocation().add(0.0D, 1.75D, 0.0D));
          } else {
            (armorStands.get(player))[0].teleport(player.getLocation().add(0.0D, 1.75D, 0.0D));
            (armorStands.get(player))[1].teleport(player.getLocation().add(0.0D, 2.05D, 0.0D));
          }
          if (player.getGameMode().equals(GameMode.SPECTATOR) || VanishAPI.isInvisible(player)) {
            (armorStands.get(player))[0].setCustomNameVisible(false);
            (armorStands.get(player))[1].setCustomNameVisible(false);
          } else {
            (armorStands.get(player))[0].setCustomNameVisible(true);
            (armorStands.get(player))[1].setCustomNameVisible(true);
          }
        } else {
          (armorStands.get(player))[0].setCustomNameVisible(false);
          if (!getPrefix(player).equals(name) || !PlayerJoinHandler.this.playersConfig.getConfig().getString("players." + player.getUniqueId() + ".status").equals(status)) {
            (armorStands.get(player))[0].setCustomName(PlayerJoinHandler.this.getStatus("§7" + PlayerJoinHandler.this.playersConfig.getConfig().getString("players." + player.getUniqueId() + ".status"), player));
            (armorStands.get(player))[1].setCustomName(PlayerJoinHandler.this.getPrefix(player));
          }
          if (player.getGameMode().equals(GameMode.SPECTATOR)) {
            (armorStands.get(player))[0].teleport(player.getLocation().add(0.0D, 50.0D, 0.0D));
            (armorStands.get(player))[1].teleport(player.getLocation().add(0.0D, 50.0D, 0.0D));
          } else if (player.isSwimming()) {
            (armorStands.get(player))[0].teleport(player.getLocation().add(0.0D, -0.35D, 0.0D));
            (armorStands.get(player))[1].teleport(player.getLocation().add(0.0D, -0.35D, 0.0D));
          } else if (player.isSneaking()) {
            (armorStands.get(player))[0].teleport(player.getLocation().add(0.0D, 1.45D, 0.0D));
            (armorStands.get(player))[1].teleport(player.getLocation().add(0.0D, 1.45D, 0.0D));
          } else {
            (armorStands.get(player))[0].teleport(player.getLocation().add(0.0D, 1.75D, 0.0D));
            (armorStands.get(player))[1].teleport(player.getLocation().add(0.0D, 1.75D, 0.0D));
          }
          if (player.getGameMode().equals(GameMode.SPECTATOR) || VanishAPI.isInvisible(player)) {
            (armorStands.get(player))[1].setCustomNameVisible(false);
          } else {
            (armorStands.get(player))[1].setCustomNameVisible(true);
          }
        }
      } else {
        armorStands.get(player)[0].remove();
        armorStands.get(player)[1].remove();
        armorStands.remove(player);
        x = 0;
        return;
      }
      x++;

      },20L, 1L);
  }
  
  private String getStatus(String status, Player color) {
    if (color.hasPermission("persival.status.color"))
      return ChatColor.translateAlternateColorCodes('&', status); 
    return status;
  }
  
  private String getPrefix(Player player) {
    String prefix = this.groupsConfig.getConfig().getString("groups." + Ignotus.perms.getPrimaryGroup(player) + ".prefix");
    String name = this.groupsConfig.getConfig().getString("groups." + Ignotus.perms.getPrimaryGroup(player) + ".player-name");
    String suffix = this.groupsConfig.getConfig().getString("groups." + Ignotus.perms.getPrimaryGroup(player) + ".suffix");
    prefix = ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, prefix));
    name = ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, name));
    suffix = ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, suffix));
    String all = prefix + name + suffix;
    return all;
  }
  
  private void setTeam(Player player, Scoreboard score) {
    Team t = score.getTeam(this.groupsConfig.getConfig().getString("tablist." + Ignotus.perms.getPrimaryGroup(player) + ".sort") + Ignotus.perms.getPrimaryGroup(player));
    if (t == null) {
      t = score.registerNewTeam(this.groupsConfig.getConfig().getString("tablist." + Ignotus.perms.getPrimaryGroup(player) + ".sort") + Ignotus.perms.getPrimaryGroup(player));
      t.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
    } 
    String prefix = this.groupsConfig.getConfig().getString("tablist." + Ignotus.perms.getPrimaryGroup(player) + ".prefix");
    prefix = ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, prefix));
    String name = this.groupsConfig.getConfig().getString("tablist." + Ignotus.perms.getPrimaryGroup(player) + ".player-name");
    name = ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, name));
    String suffix = this.groupsConfig.getConfig().getString("tablist." + Ignotus.perms.getPrimaryGroup(player) + ".suffix");
    suffix = ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, suffix));
    t.setPrefix(this.groupsConfig.getConfig().getString("tablist." + Ignotus.perms.getPrimaryGroup(player) + ".sort"));
    player.setPlayerListName(prefix + name + suffix);
    t.addEntry(player.getName());
  }

  public void clearNameTags() {
    for (Map.Entry<Player, ArmorStand[]> armorStand : armorStands.entrySet()) {
      armorStands.get(armorStand.getKey())[0].remove();
      armorStands.get(armorStand.getKey())[1].remove();
      armorStands.remove(armorStand.getKey());
    }
  }
}
