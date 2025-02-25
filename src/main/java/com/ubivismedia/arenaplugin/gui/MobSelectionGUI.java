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
import java.util.List;

public class MobSelectionGUI {
    
    private final Arena arena;
    private final Block configBlock;
    private final Inventory gui;
    private final List<String> availableMobs;
    
    public MobSelectionGUI(Arena arena, Block configBlock) {
        this.arena = arena;
        this.configBlock = configBlock;
        this.availableMobs = arena.getAvailableMobs();
        this.gui = Bukkit.createInventory(null, 54, "Gegner auswählen");
        setupItems();
    }
    
    private void setupItems() {
        int slot = 0;
        for (String mob : availableMobs) {
            if (slot >= 54) break; // Begrenzung auf 54 Slots
            gui.setItem(slot++, createMobItem(mob));
        }
        gui.setItem(53, createItem(Material.BARRIER, "Schließen"));
    }
    
    private ItemStack createMobItem(String mobName) {
        ItemStack item = new ItemStack(Material.ZOMBIE_SPAWN_EGG);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(mobName);
            item.setItemMeta(meta);
        }
        return item;
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
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || !clickedItem.hasItemMeta()) return;
        
        String selectedMob = clickedItem.getItemMeta().getDisplayName();
        if (selectedMob.equals("Schließen")) {
            player.closeInventory();
            return;
        }
        
        arena.setMobType(configBlock, selectedMob);
        player.sendMessage("Gegnertyp für diesen Block auf " + selectedMob + " gesetzt!");
        player.closeInventory();
    }
}
