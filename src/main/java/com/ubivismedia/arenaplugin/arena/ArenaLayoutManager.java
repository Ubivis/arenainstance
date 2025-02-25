package com.ubivismedia.arenaplugin.arena;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import java.util.HashMap;
import java.util.Map;

public class ArenaLayoutManager {
    
    private final Map<String, ArenaLayout> arenaLayouts = new HashMap<>();
    
    public void createArenaLayout(String arenaName, World world, Location corner1, Location corner2) {
        ArenaLayout layout = new ArenaLayout(world, corner1, corner2);
        arenaLayouts.put(arenaName, layout);
        Bukkit.broadcastMessage("Arena-Layout für " + arenaName + " wurde gespeichert!");
    }
    
    public void loadArenaLayout(String arenaName, World world, Location startLocation) {
        if (!arenaLayouts.containsKey(arenaName)) {
            Bukkit.getLogger().warning("Kein Layout für Arena " + arenaName + " gefunden!");
            return;
        }
        arenaLayouts.get(arenaName).applyLayout(world, startLocation);
    }
    
    private static class ArenaLayout {
        private final World world;
        private final Location corner1;
        private final Location corner2;
        private final Map<Location, Material> blockData = new HashMap<>();
        
        public ArenaLayout(World world, Location corner1, Location corner2) {
            this.world = world;
            this.corner1 = corner1;
            this.corner2 = corner2;
            saveLayout();
        }
        
        private void saveLayout() {
            int minX = Math.min(corner1.getBlockX(), corner2.getBlockX());
            int minY = Math.min(corner1.getBlockY(), corner2.getBlockY());
            int minZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ());
            int maxX = Math.max(corner1.getBlockX(), corner2.getBlockX());
            int maxY = Math.max(corner1.getBlockY(), corner2.getBlockY());
            int maxZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ());
            
            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        Location loc = new Location(world, x, y, z);
                        blockData.put(loc, loc.getBlock().getType());
                    }
                }
            }
        }
        
        public void applyLayout(World targetWorld, Location startLocation) {
            for (Map.Entry<Location, Material> entry : blockData.entrySet()) {
                Location relativeLocation = entry.getKey().clone().subtract(corner1).add(startLocation);
                Block block = targetWorld.getBlockAt(relativeLocation);
                block.setType(entry.getValue());
            }
        }
    }
}
