package com.ubivismedia.arenaplugin.listeners;

import com.ubivismedia.arenaplugin.arena.Arena;
import com.ubivismedia.arenaplugin.gui.ArenaEditGUI;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SpecialBlockListener implements Listener {
    
    private final Arena arena;
    
    public SpecialBlockListener(Arena arena) {
        this.arena = arena;
    }
    
    @EventHandler
    public void onBlockInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        
        Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }
        
        Player player = event.getPlayer();
        
        // Prüfen, ob der Block ein Spezialblock ist
        if (block.getType() == Material.EMERALD_BLOCK) {
            new ArenaEditGUI(arena, block).open(player);
            event.setCancelled(true);
        }
    }
}
