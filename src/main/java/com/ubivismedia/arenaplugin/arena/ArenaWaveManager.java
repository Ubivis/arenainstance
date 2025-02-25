package com.ubivismedia.arenaplugin.arena;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.List;
import java.util.UUID;

public class ArenaWaveManager {
    
    private final Arena arena;
    private int currentWave = 1;
    private boolean battleActive = true;
    
    public ArenaWaveManager(Arena arena) {
        this.arena = arena;
    }
    
    public void startWaves(World world) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!battleActive || areAllPlayersDead()) {
                    endBattle();
                    cancel();
                    return;
                }
                spawnWave(world);
                currentWave++;
            }
        }.runTaskTimer(arena.getPlugin(), 0L, 20L * arena.getWaveInterval());
    }
    
    private void spawnWave(World world) {
        List<Block> waveBlocks = arena.getWaveBlocks();
        
        for (Block block : waveBlocks) {
            if (arena.getWaveNumber(block) == currentWave) {
                int amount = arena.getWaveAmount(block);
                String mobType = arena.getMobType(block);
                
                for (int i = 0; i < amount; i++) {
                    Location spawnLocation = block.getLocation().add(0, 1, 0);
                    LivingEntity entity = (LivingEntity) world.spawnEntity(spawnLocation, EntityType.valueOf(mobType));
                    entity.setCustomName("Welle " + currentWave + " - " + mobType);
                }
            }
        }
    }
    
    private boolean areAllPlayersDead() {
        for (UUID playerId : arena.getPlayersInArena()) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null && !player.isDead()) {
                return false;
            }
        }
        return true;
    }
    
    private void endBattle() {
        battleActive = false;
        Bukkit.broadcastMessage("Die Arena " + arena.getName() + " wurde beendet! Alle Wellen besiegt oder alle Spieler tot.");
        arena.resetArena();
    }
}
