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

public class PlayerBlockOptionsGUI {
    
    private final Arena arena;
    private final Block configBlock;
    private final Inventory gui;
    
    public PlayerBlockOptionsGUI(Arena arena, Block configBlock) {
        this.arena = arena;
        this.configBlock = configBlock;
        this.gui = Bukkit.createInventory(null, 9, "Spielerblock-Einstellungen");
        setupItems();
    }
    
    private void setupItems() {
        gui.setItem(0, createItem(Material.BARRIER, "Block für den Live-Modus unsichtbar machen"));
        gui.setItem(1, createItem(Material.ENDER_EYE, "Entity-Typ ändern"));
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
                arena.togglePlayerBlockVisibility(configBlock);
                player.sendMessage("Sichtbarkeit des Spielerblocks geändert!");
                break;
            case 1:
                new EntityTypeSelectionGUI(arena, configBlock).open(player);
                break;
            case 8:
                player.closeInventory();
                break;
        }
        setupItems();
    }
}
