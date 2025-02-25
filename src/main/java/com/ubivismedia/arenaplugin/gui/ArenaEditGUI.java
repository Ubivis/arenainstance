package com.ubivismedia.arenaplugin.gui;

import com.ubivismedia.arenaplugin.arena.Arena;
import com.ubivismedia.arenaplugin.arena.ArenaPvPManager;
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
    private final ArenaPvPManager pvpManager;
    
    public ArenaEditGUI(Arena arena, Block configBlock, ArenaPvPManager pvpManager) {
        this.arena = arena;
        this.configBlock = configBlock;
        this.pvpManager = pvpManager;
        this.gui = Bukkit.createInventory(null, 9, "Block-Einstellungen");
        setupItems();
    }
    
    private void setupItems() {
        gui.setItem(0, createItem(Material.IRON_SWORD, "PvP: " + (pvpManager.isPvPEnabled(arena.getName()) ? "Aktiviert" : "Deaktiviert")));
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
                boolean newPvPStatus = !pvpManager.isPvPEnabled(arena.getName());
                pvpManager.setPvPEnabled(arena.getName(), newPvPStatus);
                player.sendMessage("PvP wurde " + (newPvPStatus ? "aktiviert" : "deaktiviert") + "!");
                break;
            case 8:
                player.closeInventory();
                break;
        }
        setupItems();
    }
}
