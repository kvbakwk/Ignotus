package me.thekuba.files;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

import me.thekuba.Ignotus;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class PlayersManager {
  private final Ignotus plugin;
  private FileConfiguration playersConfig;
  private File configFile;
  
  public PlayersManager(Ignotus plugin) {
    this.plugin = plugin;
    saveDefaultConfig();
  }
  
  public void reloadConfig() {
    if (this.configFile == null)
      this.configFile = new File(this.plugin.getDataFolder(), "players.yml");

    this.playersConfig = YamlConfiguration.loadConfiguration(this.configFile);
    InputStream defaultStream = this.plugin.getResource("players.yml");

    if (defaultStream != null) {
      YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
      this.playersConfig.setDefaults(defaultConfig);
    } 
  }
  
  public FileConfiguration getConfig() {
    if (this.playersConfig == null)
      reloadConfig();

    return this.playersConfig;
  }
  
  public void saveConfig() {
    if (this.playersConfig == null || this.configFile == null)
      return;

    try {
      getConfig().save(this.configFile);
    } catch (IOException e) {
      this.plugin.getLogger().log(Level.SEVERE, "Nie można zapisać pliku " + this.configFile, e);
    } 
  }
  
  public void saveDefaultConfig() {
    if (this.configFile == null)
      this.configFile = new File(this.plugin.getDataFolder(), "players.yml");

    if (!this.configFile.exists())
      this.plugin.saveResource("players.yml", false); 
  }
}
