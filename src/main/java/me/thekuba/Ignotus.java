package me.thekuba;

import me.thekuba.commands.*;
import me.thekuba.handlers.*;
import me.thekuba.inventories.AbyssInventory;
import me.thekuba.managers.AbyssManager;
import me.thekuba.managers.DatabaseManager;
import me.thekuba.managers.NametagManager;
import me.thekuba.managers.TablistManager;
import me.thekuba.placeholders.IgnotusExpansion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.permission.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Ignotus extends JavaPlugin implements Listener {

    public IgnotusFile configFile, playersFile, groupsFile, messagesFile;
    public DamageHandler pvp;
    public AbyssManager abyss;
    public NametagManager nametag;
    public List<Inventory> abyssInv = new ArrayList<>();
    public static Permission perms;
    public DatabaseManager database;

    private final String[] depends = {"NBTAPI", "Vault"};

    @Override
    public void onEnable() {
        this.configFile = new IgnotusFile(this, "config");
        this.groupsFile = new IgnotusFile(this, "groups");
        this.messagesFile = new IgnotusFile(this, "messages");

        if(getConfig().getBoolean("mysql.enable")) {
            this.database = new DatabaseManager(this);
        } else {
            this.playersFile = new IgnotusFile(this, "players");
            database = null;
        }

        for (String depend : depends)
            if (Bukkit.getPluginManager().getPlugin(depend) == null) {
                toConsoleWarn("You don't have " + depend + "! This plugin is required.");
                Bukkit.getPluginManager().disablePlugin(this);
            }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            (new IgnotusExpansion()).register();

        setupPermissions();

        getCommand("ignotus").setExecutor(new IgnotusCmd(this, "ignotus.cmd.ignotus"));
        getCommand("abyss").setExecutor(new AbyssCmd(this, "ignotus.cmd.abyss"));
        getCommand("self").setExecutor(new SelfCmd(this, "ignotus.cmd.self"));
        getCommand("set").setExecutor(new SetCmd(this, "ignotus.cmd.set"));
        getCommand("setadmin").setExecutor(new SetAdminCmd(this, "ignotus.cmd.admin.setadmin"));

        new PlayerClickHandler(this);
        new ClickInvHandler(this);
        new CloseInvHandler(this);

        new TablistManager(this);
        this.nametag = new NametagManager(this);
        for (Player player : Bukkit.getOnlinePlayers())
            this.nametag.addNametag(player);

        new PlayerJoinHandler(this);
        this.pvp = new DamageHandler(this);

        if (getConfig().getBoolean("abyss.enable")) {
            this.abyss = new AbyssManager(this);
            List<ItemStack> items = this.abyss.getItems();
            applyAbyss(items);
        }

        toConsoleInfo("The Ignotus plugin has been successfully loaded.");
    }
    @Override
    public void onDisable() {
        if(!Objects.equals(nametag, null))
            nametag.removeNametags();
    }


    public void applyAbyss(List<ItemStack> items) {
        int itemsSize;
        if(!Objects.equals(items, null))
            itemsSize = items.size();
        else {
            itemsSize = 0;
            items = new ArrayList<>();
        }
        this.abyssInv = new ArrayList<>();
        List<ItemStack> items2 = new ArrayList<>();
        for (ItemStack item : items)
            items2.add(item);
        if (itemsSize > 36) {
            for (int i = 0; i < itemsSize / 36 + 1; i++) {
                List<ItemStack> itemsOne = new ArrayList<>();
                for (int i2 = 0; i2 < 36; i2++) {
                    if (i2 == items2.size())
                        break;
                    itemsOne.add(items2.get(i2));
                    items.remove(0);
                }
                items2.clear();
                for (ItemStack item : items)
                    items2.add(item);
                if (i == 0) {
                    AbyssInventory invTemp = new AbyssInventory(itemsOne, 0, i);
                    IgnotusItem itemTemp = new IgnotusItem(invTemp.inv.getItem(0));
                    itemTemp.setIntNBT("abyssValue", i);
                    this.abyssInv.add(invTemp.inv);
                } else if (i == itemsSize / 36) {
                    AbyssInventory invTemp = new AbyssInventory(itemsOne, 1, i);
                    IgnotusItem itemTemp = new IgnotusItem(invTemp.inv.getItem(0));
                    itemTemp.setIntNBT("abyssValue", i);
                    this.abyssInv.add(invTemp.inv);
                } else {
                    AbyssInventory invTemp = new AbyssInventory(itemsOne, 2, i);
                    IgnotusItem itemTemp = new IgnotusItem(invTemp.inv.getItem(0));
                    itemTemp.setIntNBT("abyssValue", i);
                    this.abyssInv.add(invTemp.inv);
                }
            }
        } else {
            AbyssInventory invTemp = new AbyssInventory(items, 3, 0);
            IgnotusItem itemTemp = new IgnotusItem(invTemp.inv.getItem(0));
            itemTemp.setIntNBT("abyssValue", 0);
            this.abyssInv.add(invTemp.inv);
        }
    }
    public static void toConsoleInfo(String msg) {
        Bukkit.getLogger().info("[Ignotus] " + msg);
    }
    public static void toConsoleWarn(String msg) {
        Bukkit.getLogger().warning("[Ignotus]   " + msg);
    }
    public String colorCodes(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return (perms != null);
    }
}
