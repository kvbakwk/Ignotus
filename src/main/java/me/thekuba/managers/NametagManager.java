package me.thekuba.managers;

import de.myzelyam.api.vanish.VanishAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import me.thekuba.Ignotus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NametagManager {

    private final Ignotus plugin;
    private FileConfiguration playersConfig, groupsConfig;

    private int x;
    private final Map<Player, ArmorStand[]> armorStands = new HashMap<>();


    public NametagManager(Ignotus plugin) {
        this.plugin = plugin;
        if(Objects.equals(plugin.database, null))
            this.playersConfig = plugin.playersFile.getConfig();
        this.groupsConfig = plugin.groupsFile.getConfig();
    }

    public void addNametag(Player player) {
        final Scoreboard score = Bukkit.getScoreboardManager().getMainScoreboard();
        this.setTeam(player, score);
        ArmorStand[] armorStand;
        final String name = getPrefix(player);

        String playerStatus;

        if(Objects.equals(plugin.database, null))
            playerStatus = "§7" + this.playersConfig.getString("players." + player.getUniqueId() + ".status");
        else
            playerStatus = "§7" + plugin.database.getValue(player, "status");

        if (!playerStatus.equals("§7off"))
            armorStand = new ArmorStand[] { (ArmorStand)player.getWorld().spawnEntity(player.getLocation().add(0.0D, 1.75D, 0.0D), EntityType.ARMOR_STAND), (ArmorStand)player.getWorld().spawnEntity(player.getLocation().add(0.0D, 2.05D, 0.0D), EntityType.ARMOR_STAND) };
        else
            armorStand = new ArmorStand[] { (ArmorStand)player.getWorld().spawnEntity(player.getLocation().add(0.0D, 1.75D, 0.0D), EntityType.ARMOR_STAND), (ArmorStand)player.getWorld().spawnEntity(player.getLocation().add(0.0D, 1.75D, 0.0D), EntityType.ARMOR_STAND) };
        this.armorStands.put(player, armorStand);


        x = 0;
        Bukkit.getScheduler().runTaskTimer(this.plugin, () -> {

            String status;

            if(Objects.equals(plugin.database, null))
                status = "§7" + this.playersConfig.getString("players." + player.getUniqueId() + ".status");
            else
                status = "§7" + plugin.database.getValue(player, "status");

            if (x % 20 == 0)
                this.setTeam(player, score);

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
                            a.teleport(playerT.getLocation().add(0.0D, 1.05D, 0.0D));
                        }
                    armorStands.get(playerT)[0].setCustomName(this.getStatus(playerStatus, playerT));
                    armorStands.get(playerT)[1].setCustomName(this.getPrefix(playerT));
                }

            if (armorStands.isEmpty() || armorStands.get(player) == null)
                return;

            if (armorStands.get(player)[1] != null && player.isOnline()) {
                if (!status.equals("§7off")) {
                    (armorStands.get(player))[0].setCustomNameVisible(true);
                    if (!getPrefix(player).equals(name) || !status.equals(playerStatus)) {
                        (armorStands.get(player))[0].setCustomName(getStatus(status, player));
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
                    if (player.getGameMode().equals(GameMode.SPECTATOR) || VanishAPI.isInvisible(player) || player.isSneaking()) {
                        (armorStands.get(player))[0].setCustomNameVisible(false);
                        (armorStands.get(player))[1].setCustomNameVisible(false);
                    } else {
                        (armorStands.get(player))[0].setCustomNameVisible(true);
                        (armorStands.get(player))[1].setCustomNameVisible(true);
                    }
                } else {
                    (armorStands.get(player))[0].setCustomNameVisible(false);
                    if (!getPrefix(player).equals(name) || !status.equals(playerStatus)) {
                        (armorStands.get(player))[0].setCustomName(this.getStatus(status, player));
                        (armorStands.get(player))[1].setCustomName(this.getPrefix(player));
                    }
                    if (player.getGameMode().equals(GameMode.SPECTATOR)) {
                        (armorStands.get(player))[0].teleport(player.getLocation().add(0.0D, 50.0D, 0.0D));
                        (armorStands.get(player))[1].teleport(player.getLocation().add(0.0D, 50.0D, 0.0D));
                    } else if (player.isSwimming()) {
                        (armorStands.get(player))[0].teleport(player.getLocation().add(0.0D, 0.3D, 0.0D));
                        (armorStands.get(player))[1].teleport(player.getLocation().add(0.0D, 0.3D, 0.0D));
                    } else if (player.isSneaking()) {
                        (armorStands.get(player))[0].teleport(player.getLocation().add(0.0D, 1.45D, 0.0D));
                        (armorStands.get(player))[1].teleport(player.getLocation().add(0.0D, 1.45D, 0.0D));
                    } else {
                        (armorStands.get(player))[0].teleport(player.getLocation().add(0.0D, 1.75D, 0.0D));
                        (armorStands.get(player))[1].teleport(player.getLocation().add(0.0D, 1.75D, 0.0D));
                    }
                    if (player.getGameMode().equals(GameMode.SPECTATOR) || VanishAPI.isInvisible(player) || player.isSneaking())
                        (armorStands.get(player))[1].setCustomNameVisible(false);
                    else
                        (armorStands.get(player))[1].setCustomNameVisible(true);
                }
            } else {
                armorStands.get(player)[0].remove();
                armorStands.get(player)[1].remove();
                armorStands.remove(player);
                x = 0;
                return;
            }
            x++;

        },0L, 1L);
    }
    public void removeNametags() {
        for (Map.Entry<Player, ArmorStand[]> armorStand : armorStands.entrySet()) {
            armorStands.get(armorStand.getKey())[0].remove();
            armorStands.get(armorStand.getKey())[1].remove();
            armorStands.remove(armorStand.getKey());
        }
    }

    private String getStatus(String status, Player color) {
        if (color.hasPermission("ignotus.status.color"))
            return ChatColor.translateAlternateColorCodes('&', status);
        return status;
    }
    private String getPrefix(Player player) {
        String prefix = this.groupsConfig.getString("groups." + Ignotus.perms.getPrimaryGroup(player) + ".prefix");
        String name = this.groupsConfig.getString("groups." + Ignotus.perms.getPrimaryGroup(player) + ".player-name");
        String suffix = this.groupsConfig.getString("groups." + Ignotus.perms.getPrimaryGroup(player) + ".suffix");
        prefix = ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, prefix));
        name = ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, name));
        suffix = ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, suffix));
        return prefix + name + suffix;
    }
    private void setTeam(Player player, Scoreboard score) {
        Team t = score.getTeam(this.groupsConfig.getString("tablist." + Ignotus.perms.getPrimaryGroup(player) + ".sort") + Ignotus.perms.getPrimaryGroup(player));
        if (t == null) {
            t = score.registerNewTeam(this.groupsConfig.getString("tablist." + Ignotus.perms.getPrimaryGroup(player) + ".sort") + Ignotus.perms.getPrimaryGroup(player));
            t.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        }
        String prefix = this.groupsConfig.getString("tablist." + Ignotus.perms.getPrimaryGroup(player) + ".prefix");
        prefix = ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, prefix));
        String name = this.groupsConfig.getString("tablist." + Ignotus.perms.getPrimaryGroup(player) + ".player-name");
        name = ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, name));
        String suffix = this.groupsConfig.getString("tablist." + Ignotus.perms.getPrimaryGroup(player) + ".suffix");
        suffix = ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, suffix));
        t.setPrefix(this.groupsConfig.getString("tablist." + Ignotus.perms.getPrimaryGroup(player) + ".sort"));
        player.setPlayerListName(prefix + name + suffix);
        t.addEntry(player.getName());
    }
}
