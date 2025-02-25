package com.ubivismedia.arenaplugin.gui;

import com.ubivismedia.arenaplugin.arena.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ArenaEditGUI {
    
    private final Arena arena;
    private final Block configBlock;
    private final Inventory gui;
    
    public ArenaEditGUI(Arena arena, Block configBlock) {
        this.arena = arena;
        this.configBlock = configBlock;
        this.gui = Bukkit.createInventory(null, 9, "Block-Einstellungen");
        setupItems();
    }
    
    private void setupItems() {
        if (configBlock.getType() == Material.EMERALD_BLOCK) {
            gui.setItem(0, createItem(Material.ZOMBIE_SPAWN_EGG, "Gegner-Typ: " + arena.getMobType(configBlock)));
            gui.setItem(1, createItem(Material.PAPER, "Gegneranzahl: " + arena.getWaveAmount(configBlock)));
            gui.setItem(2, createItem(Material.CLOCK, "Spawn-Intervall: " + arena.getWaveInterval(configBlock) + "s"));
            gui.setItem(3, createItem(Material.BOOK, "Wellen-Nummer: " + arena.getWaveNumber(configBlock)));
        }
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
                arena.cycleMobType(configBlock);
                break;
            case 1:
                arena.increaseWaveAmount(configBlock);
                break;
            case 2:
                arena.increaseWaveInterval(configBlock);
                break;
            case 3:
                arena.increaseWaveNumber(configBlock);
                break;
            case 8:
                player.closeInventory();
                break;
        }
        setupItems();
    }
}
