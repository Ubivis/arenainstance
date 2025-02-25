package com.ubivismedia.arenaplugin.gui;

import com.ubivismedia.arenaplugin.arena.Arena;
import com.ubivismedia.arenaplugin.arena.ArenaSpectatorManager;
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
    private final ArenaSpectatorManager spectatorManager;
    
    public ArenaEditGUI(Arena arena, Block configBlock, ArenaSpectatorManager spectatorManager) {
        this.arena = arena;
        this.configBlock = configBlock;
        this.spectatorManager = spectatorManager;
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
        if (configBlock.getType() == Material.DIAMOND_BLOCK) {
            gui.setItem(4, createItem(Material.PLAYER_HEAD, "Zuschauerblock setzen"));
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
                new MobSelectionGUI(arena, configBlock).open(player);
                break;
            case 1:
                arena.setWaveAmount(configBlock, arena.getWaveAmount(configBlock) + 1);
                break;
            case 2:
                arena.setWaveInterval(configBlock, arena.getWaveInterval(configBlock) + 1);
                break;
            case 3:
                arena.setWaveNumber(configBlock, arena.getWaveNumber(configBlock) + 1);
                break;
            case 4:
                spectatorManager.addSpectatorBlock(arena.getName(), configBlock.getLocation());
                player.sendMessage("Zuschauerblock hinzugefügt!");
                break;
            case 8:
                player.closeInventory();
                break;
        }
        setupItems();
    }
}
