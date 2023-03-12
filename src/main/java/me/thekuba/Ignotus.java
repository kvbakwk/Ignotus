package me.thekuba;

import me.thekuba.handlers.*;
import me.thekuba.inventories.AbyssInventory;
import me.thekuba.managers.AbyssManager;
import me.thekuba.managers.CommandManager;
import me.thekuba.managers.TabManager;
import me.thekuba.placeholders.IgnotusExpansion;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import me.thekuba.files.PlayersFile;
import me.thekuba.files.GroupsFile;
import net.milkbowl.vault.permission.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Ignotus extends JavaPlugin implements Listener {

    public AbyssManager clear;
    public PlayersFile playersFile;
    public GroupsFile groupsFile;
    public DamageHandler pvp;
    public static Permission perms;
    public List<Inventory> abyssInv = new ArrayList<>();
    public PlayerJoinHandler playerJoin;


    @Override
    public void onEnable() {
        this.playersFile = new PlayersFile(this);
        this.groupsFile = new GroupsFile(this);
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

        new CommandManager(this);

        new PlayerClickHandler(this);
        new ClickInvHandler(this);
        new CloseInvHandler(this);
        new TabManager(this);
        playerJoin = new PlayerJoinHandler(this);

        this.pvp = new DamageHandler(this);

        if (getConfig().getBoolean("abyss.enable")) {
            this.clear = new AbyssManager(this);
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

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return (perms != null);
    }
}
