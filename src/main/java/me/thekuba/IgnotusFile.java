package me.thekuba;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class IgnotusFile {

    private final Ignotus plugin;
    private String fileName;
    private FileConfiguration configFileConfiguration;
    private File configFile;

    public IgnotusFile(Ignotus plugin, String name) {
        this.plugin = plugin;
        this.fileName = name;
        saveDefaultConfig();
    }

    public void reloadConfig() {
        if (this.configFile == null)
            this.configFile = new File(this.plugin.getDataFolder(), this.fileName + ".yml");

        this.configFileConfiguration = YamlConfiguration.loadConfiguration(this.configFile);
        InputStream defaultStream = this.plugin.getResource(this.fileName + ".yml");

        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.configFileConfiguration.setDefaults(defaultConfig);
        }
    }
    public FileConfiguration getConfig() {
        if (this.configFileConfiguration == null)
            reloadConfig();

        return this.configFileConfiguration;
    }
    public void saveConfig() {
        if (this.configFileConfiguration == null || this.configFile == null)
            return;

        try {
            getConfig().save(this.configFile);
        } catch (IOException e) {
            this.plugin.getLogger().log(Level.SEVERE, "Nie można zapisać pliku " + this.configFile, e);
        }
    }
    public void saveDefaultConfig() {
        if (this.configFile == null)
            this.configFile = new File(this.plugin.getDataFolder(), this.fileName + ".yml");

        if (!this.configFile.exists())
            this.plugin.saveResource(this.fileName + ".yml", false);
    }

}
