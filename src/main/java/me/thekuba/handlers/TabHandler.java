package me.thekuba.handlers;

import me.clip.placeholderapi.PlaceholderAPI;
import me.thekuba.Ignotus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class TabHandler {
    private final Ignotus plugin;
    private final FileConfiguration config;

    public TabHandler (Ignotus plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();

        showTab();
    }

    public void showTab() {

        if(!config.getBoolean("tablist.enable"))
            return;

        List<String> headers = config.getStringList("tablist.headers");
        List<String> footers = config.getStringList("tablist.footers");

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

            int counter = 0, countH = 0, countF = 0;

            @Override
            public void run() {
                if(counter % config.getInt("tablist.refreshFrequency") == 0){

                    if(Bukkit.getOnlinePlayers().size() != 0)
                        for(Player player : Bukkit.getOnlinePlayers()) {
                            player.setPlayerListHeader(format(player, headers.get(countH)));
                            player.setPlayerListFooter(format(player, footers.get(countF)));
                        }

                    if(counter % config.getInt("tablist.frequency") == 0){
                        countH++;
                        countF++;
                        if(countH == headers.size())
                            countH = 0;
                        if(countF == footers.size())
                            countF = 0;
                    }

                    if(counter > 100000) {
                        counter = 0;
                    }
                }
                counter++;
            }
        }, 10, 1);
    }

    private String format(Player player, String msg) {
        return ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, msg));
    }


}
