package com.ubivismedia.arenaplugin.gui;

import com.ubivismedia.arenaplugin.arena.ArenaModeManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ArenaModeGUI {
    
    private final String arenaName;
    private final ArenaModeManager modeManager;
    private final Inventory gui;
    
    public ArenaModeGUI(String arenaName, ArenaModeManager modeManager) {
        this.arenaName = arenaName;
        this.modeManager = modeManager;
        this.gui = Bukkit.createInventory(null, 9, "Arena-Modus wählen");
        setupItems();
    }
    
    private void setupItems() {
        gui.setItem(0, createItem(Material.IRON_SWORD, "Survival-Modus"));
        gui.setItem(1, createItem(Material.WITHER_SKELETON_SKULL, "Boss-Modus"));
        gui.setItem(2, createItem(Material.CLOCK, "Speedrun-Modus"));
        gui.setItem(8, createItem(Material.BARRIER, "Schließen"));
    }
    
    private ItemStack createItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }
    
    public void open(Player player) {
        player.openInventory(gui);
    }
    
    public void handleClick(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        
        switch (slot) {
            case 0:
                modeManager.setArenaMode(arenaName, ArenaModeManager.ArenaMode.SURVIVAL);
                player.sendMessage("Arena-Modus auf Survival gesetzt!");
                break;
            case 1:
                modeManager.setArenaMode(arenaName, ArenaModeManager.ArenaMode.BOSS);
                player.sendMessage("Arena-Modus auf Boss gesetzt!");
                break;
            case 2:
                modeManager.setArenaMode(arenaName, ArenaModeManager.ArenaMode.SPEEDRUN);
                player.sendMessage("Arena-Modus auf Speedrun gesetzt!");
                break;
            case 8:
                player.closeInventory();
                break;
        }
    }
}
