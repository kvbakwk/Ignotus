package me.thekuba;

import me.thekuba.commands.Abyss;
import me.thekuba.commands.Self;
import me.thekuba.commands.Set;
import me.thekuba.commands.Setadmin;
import me.thekuba.handlers.*;
import me.thekuba.inventories.AbyssInventory;
import me.thekuba.items.ItemIgnotus;
import me.thekuba.placeholders.IgnotusExpansion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import me.thekuba.files.PlayersManager;
import me.thekuba.files.GroupsManager;
import net.milkbowl.vault.permission.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Ignotus extends JavaPlugin implements Listener {

    public ClearHandler clear;
    public PlayersManager playersFile;
    public GroupsManager groupsFile;
    public DamageHandler pvp;
    public static Permission perms;
    public List<Inventory> abyssInv = new ArrayList<>();
    public PlayerJoinHandler playerJoin;


    @Override
    public void onEnable() {
        this.playersFile = new PlayersManager(this);
        this.groupsFile = new GroupsManager(this);
        saveDefaultConfig();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            toConsoleWarn("You don't have PlaceholderAPI! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
        } else {
            (new IgnotusExpansion()).register();
        }
        if (Bukkit.getPluginManager().getPlugin("NBTAPI") == null) {
            toConsoleWarn("You don't have NBTAPI! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            toConsoleWarn("You don't have Vault! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        setupPermissions();

        getCommand("set").setExecutor(new Set());
        getCommand("setadmin").setExecutor(new Setadmin());
        getCommand("abyss").setExecutor(new Abyss());
        getCommand("self").setExecutor(new Self());

        new PlayerClickHandler(this);
        new ClickInvHandler(this);
        new CloseInvHandler(this);
        playerJoin = new PlayerJoinHandler(this);

        this.pvp = new DamageHandler(this);

        if (getConfig().getBoolean("abyss.enable")) {
            this.clear = new ClearHandler(this);
            List<ItemStack> items = this.clear.getItems();
            applyAbyss(items);
        }

        toConsoleInfo("The Ignotus plugin has been successfully loaded.");
    }
    @Override
    public void onDisable() {
        playerJoin.clearNameTags();
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
                    ItemIgnotus itemTemp = new ItemIgnotus(invTemp.inv.getItem(0));
                    itemTemp.setIntNBT("abyssValue", i);
                    this.abyssInv.add(invTemp.inv);
                } else if (i == itemsSize / 36) {
                    AbyssInventory invTemp = new AbyssInventory(itemsOne, 1, i);
                    ItemIgnotus itemTemp = new ItemIgnotus(invTemp.inv.getItem(0));
                    itemTemp.setIntNBT("abyssValue", i);
                    this.abyssInv.add(invTemp.inv);
                } else {
                    AbyssInventory invTemp = new AbyssInventory(itemsOne, 2, i);
                    ItemIgnotus itemTemp = new ItemIgnotus(invTemp.inv.getItem(0));
                    itemTemp.setIntNBT("abyssValue", i);
                    this.abyssInv.add(invTemp.inv);
                }
            }
        } else {
            AbyssInventory invTemp = new AbyssInventory(items, 3, 0);
            ItemIgnotus itemTemp = new ItemIgnotus(invTemp.inv.getItem(0));
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

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return (perms != null);
    }
}
