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

public class PlayerJoinHandler implements Listener {
  private final Ignotus plugin;
  private final PlayersManager playersConfig;
  private final GroupsManager groupsConfig;
  
  private final Map<Player, ArmorStand[]> armorStands = new HashMap<>();
  
  public PlayerJoinHandler(Ignotus plugin) {
    this.plugin = plugin;
    this.playersConfig = plugin.playersFile;
    this.groupsConfig = plugin.groupsFile;
    Bukkit.getPluginManager().registerEvents(this, plugin);
  }
  
  @EventHandler
  public void onPlayerInteract(PlayerJoinEvent event) {

    ArmorStand[] armor2;
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

    final Scoreboard score = Bukkit.getScoreboardManager().getMainScoreboard();
    if (!this.plugin.getConfig().getBoolean("nametags.enable"))
      return; 
    setTeam(player, score);

    final String name = getPrefix(player);
    if (!this.playersConfig.getConfig().getString("players." + player.getUniqueId() + ".status").equals("off"))
      armor2 = new ArmorStand[] { (ArmorStand)player.getWorld().spawnEntity(player.getLocation().add(0.0D, 1.75D, 0.0D), EntityType.ARMOR_STAND), (ArmorStand)player.getWorld().spawnEntity(player.getLocation().add(0.0D, 2.05D, 0.0D), EntityType.ARMOR_STAND) };
    else
      armor2 = new ArmorStand[] { (ArmorStand)player.getWorld().spawnEntity(player.getLocation().add(0.0D, 1.75D, 0.0D), EntityType.ARMOR_STAND), (ArmorStand)player.getWorld().spawnEntity(player.getLocation().add(0.0D, 1.75D, 0.0D), EntityType.ARMOR_STAND) };
    this.armorStands.put(player, armor2);

    ArmorStand[] armorStand = this.armorStands.get(player);
    this.armorStands.replace(player, armorStand);
    final String status = "§7" + this.playersConfig.getConfig().getString("players." + player.getUniqueId() + ".status");
    Bukkit.getScheduler().runTaskTimer(this.plugin, new Runnable() {
          int x = 0;

          public void run() {
            if (this.x % 100 == 0)
              PlayerJoinHandler.this.setTeam(player, score);

            if (this.x == 0 && PlayerJoinHandler.this.armorStands.get(player) != null)
              for (Player playerT : Bukkit.getServer().getOnlinePlayers()) {
                if (PlayerJoinHandler.this.armorStands.get(playerT) != null)
                  for (ArmorStand a : PlayerJoinHandler.this.armorStands.get(playerT)) {
                    a.setGravity(false);
                    a.setCustomNameVisible(true);
                    a.setSmall(true);
                    a.setBasePlate(false);
                    a.setVisible(false);
                    a.setMarker(true);
                    a.setCustomName(PlayerJoinHandler.this.getPrefix(playerT));
                    a.setCustomName(PlayerJoinHandler.this.getStatus("§7" + PlayerJoinHandler.this.playersConfig.getConfig().getString("players." + player.getUniqueId() + ".status"), playerT));
                    a.teleport(playerT.getLocation().add(0.0D, 1.05D, 0.0D));
                  }  
              }

            if (PlayerJoinHandler.this.armorStands.isEmpty() || PlayerJoinHandler.this.armorStands.get(player) == null)
              return;

            if ((PlayerJoinHandler.this.armorStands.get(player))[1] != null && player.isOnline()) {
              if (!PlayerJoinHandler.this.playersConfig.getConfig().getString("players." + player.getUniqueId() + ".status").equals("off")) {
                (PlayerJoinHandler.this.armorStands.get(player))[0].setCustomNameVisible(true);
                if (!PlayerJoinHandler.this.getPrefix(player).equals(name) || !PlayerJoinHandler.this.playersConfig.getConfig().getString("players." + player.getUniqueId() + ".status").equals(status)) {
                  (PlayerJoinHandler.this.armorStands.get(player))[0].setCustomName(PlayerJoinHandler.this.getStatus("§7" + PlayerJoinHandler.this.playersConfig.getConfig().getString("players." + player.getUniqueId() + ".status"), player));
                  (PlayerJoinHandler.this.armorStands.get(player))[1].setCustomName(PlayerJoinHandler.this.getPrefix(player));
                } 
                if (player.getGameMode().equals(GameMode.SPECTATOR)) {
                  (PlayerJoinHandler.this.armorStands.get(player))[0].teleport(player.getLocation().add(0.0D, 50.0D, 0.0D));
                  (PlayerJoinHandler.this.armorStands.get(player))[1].teleport(player.getLocation().add(0.0D, 50.0D, 0.0D));
                } else if (player.isSwimming()) {
                  (PlayerJoinHandler.this.armorStands.get(player))[0].teleport(player.getLocation().add(0.0D, 0.3D, 0.0D));
                  (PlayerJoinHandler.this.armorStands.get(player))[1].teleport(player.getLocation().add(0.0D, 0.6D, 0.0D));
                } else if (player.isSneaking()) {
                  (PlayerJoinHandler.this.armorStands.get(player))[0].teleport(player.getLocation().add(0.0D, 1.45D, 0.0D));
                  (PlayerJoinHandler.this.armorStands.get(player))[1].teleport(player.getLocation().add(0.0D, 1.75D, 0.0D));
                } else {
                  (PlayerJoinHandler.this.armorStands.get(player))[0].teleport(player.getLocation().add(0.0D, 1.75D, 0.0D));
                  (PlayerJoinHandler.this.armorStands.get(player))[1].teleport(player.getLocation().add(0.0D, 2.05D, 0.0D));
                } 
                if (player.getGameMode().equals(GameMode.SPECTATOR) || VanishAPI.isInvisible(player)) {
                  (PlayerJoinHandler.this.armorStands.get(player))[0].setCustomNameVisible(false);
                  (PlayerJoinHandler.this.armorStands.get(player))[1].setCustomNameVisible(false);
                } else {
                  (PlayerJoinHandler.this.armorStands.get(player))[0].setCustomNameVisible(true);
                  (PlayerJoinHandler.this.armorStands.get(player))[1].setCustomNameVisible(true);
                } 
              } else {
                (PlayerJoinHandler.this.armorStands.get(player))[0].setCustomNameVisible(false);
                if (!PlayerJoinHandler.this.getPrefix(player).equals(name) || !PlayerJoinHandler.this.playersConfig.getConfig().getString("players." + player.getUniqueId() + ".status").equals(status)) {
                  (PlayerJoinHandler.this.armorStands.get(player))[0].setCustomName(PlayerJoinHandler.this.getStatus("§7" + PlayerJoinHandler.this.playersConfig.getConfig().getString("players." + player.getUniqueId() + ".status"), player));
                  (PlayerJoinHandler.this.armorStands.get(player))[1].setCustomName(PlayerJoinHandler.this.getPrefix(player));
                } 
                if (player.getGameMode().equals(GameMode.SPECTATOR)) {
                  (PlayerJoinHandler.this.armorStands.get(player))[0].teleport(player.getLocation().add(0.0D, 50.0D, 0.0D));
                  (PlayerJoinHandler.this.armorStands.get(player))[1].teleport(player.getLocation().add(0.0D, 50.0D, 0.0D));
                } else if (player.isSwimming()) {
                  (PlayerJoinHandler.this.armorStands.get(player))[0].teleport(player.getLocation().add(0.0D, -0.35D, 0.0D));
                  (PlayerJoinHandler.this.armorStands.get(player))[1].teleport(player.getLocation().add(0.0D, -0.35D, 0.0D));
                } else if (player.isSneaking()) {
                  (PlayerJoinHandler.this.armorStands.get(player))[0].teleport(player.getLocation().add(0.0D, 1.45D, 0.0D));
                  (PlayerJoinHandler.this.armorStands.get(player))[1].teleport(player.getLocation().add(0.0D, 1.45D, 0.0D));
                } else {
                  (PlayerJoinHandler.this.armorStands.get(player))[0].teleport(player.getLocation().add(0.0D, 1.75D, 0.0D));
                  (PlayerJoinHandler.this.armorStands.get(player))[1].teleport(player.getLocation().add(0.0D, 1.75D, 0.0D));
                }
                if (player.getGameMode().equals(GameMode.SPECTATOR) || VanishAPI.isInvisible(player)) {
                  (PlayerJoinHandler.this.armorStands.get(player))[1].setCustomNameVisible(false);
                } else {
                  (PlayerJoinHandler.this.armorStands.get(player))[1].setCustomNameVisible(true);
                }
              } 
            } else {
              (PlayerJoinHandler.this.armorStands.get(player))[0].remove();
              (PlayerJoinHandler.this.armorStands.get(player))[1].remove();
              PlayerJoinHandler.this.armorStands.remove(player);
              this.x = 0;
              return;
            }
            this.x++;
          }
        },20L, 1L);
  }
  
  public String getStatus(String status, Player color) {
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
}
