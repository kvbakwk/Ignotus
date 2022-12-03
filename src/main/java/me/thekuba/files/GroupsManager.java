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

public class GroupsManager {
  private final Ignotus plugin;
  
  private FileConfiguration groupsConfig = null;
  
  private File configFile = null;
  
  public GroupsManager(Ignotus plugin) {
    this.plugin = plugin;
    saveDefaultConfig();
  }
  
  public void reloadConfig() {
    if (this.configFile == null)
      this.configFile = new File(this.plugin.getDataFolder(), "groups.yml"); 
    this.groupsConfig = (FileConfiguration)YamlConfiguration.loadConfiguration(this.configFile);
    InputStream defaultStream = this.plugin.getResource("groups.yml");
    if (defaultStream != null) {
      YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
      this.groupsConfig.setDefaults((Configuration)defaultConfig);
    } 
  }
  
  public FileConfiguration getConfig() {
    if (this.groupsConfig == null)
      reloadConfig(); 
    return this.groupsConfig;
  }
  
  public void saveConfig() {
    if (this.groupsConfig == null || this.configFile == null)
      return; 
    try {
      getConfig().save(this.configFile);
    } catch (IOException e) {
      this.plugin.getLogger().log(Level.SEVERE, "Nie można zapisać pliku " + this.configFile, e);
    } 
  }
  
  public void saveDefaultConfig() {
    if (this.configFile == null)
      this.configFile = new File(this.plugin.getDataFolder(), "groups.yml"); 
    if (!this.configFile.exists())
      this.plugin.saveResource("groups.yml", false); 
  }
}
