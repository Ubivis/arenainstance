package com.ubivismedia.arenaplugin.arena;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.List;
import java.util.Random;

public class ArenaEnvironmentManager {
    
    private final Arena arena;
    private final Random random = new Random();
    
    public ArenaEnvironmentManager(Arena arena) {
        this.arena = arena;
    }
    
    public void startEnvironmentEffects(World world) {
        new BukkitRunnable() {
            @Override
            public void run() {
                int currentWave = arena.getCurrentWave();
                int totalWaves = arena.getTotalWaves();
                
                double probability = (currentWave / (double) totalWaves) * 0.5; // Maximal 50% Wahrscheinlichkeit
                if (random.nextDouble() <= probability) {
                    triggerRandomEffect(world);
                }
            }
        }.runTaskTimer(arena.getPlugin(), 0L, 200L); // Alle 10 Sekunden prüfen
    }
    
    private void triggerRandomEffect(World world) {
        int effectType = random.nextInt(4);
        switch (effectType) {
            case 0:
                activateFireTrap(world);
                break;
            case 1:
                activatePitfallTrap(world);
                break;
            case 2:
                triggerLightning(world);
                break;
            case 3:
                triggerFogEffect(world);
                break;
        }
    }
    
    private void activateFireTrap(World world) {
        List<Location> fireTrapLocations = arena.getFireTraps();
        if (fireTrapLocations.isEmpty()) return;
        
        Location loc = fireTrapLocations.get(random.nextInt(fireTrapLocations.size()));
        world.getBlockAt(loc).setType(Material.FIRE);
        world.playSound(loc, Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.0f);
    }
    
    private void activatePitfallTrap(World world) {
        List<Location> pitfallTraps = arena.getPitfallTraps();
        if (pitfallTraps.isEmpty()) return;
        
        Location loc = pitfallTraps.get(random.nextInt(pitfallTraps.size()));
        Block block = world.getBlockAt(loc);
        block.setType(Material.AIR);
        world.playSound(loc, Sound.BLOCK_PISTON_EXTEND, 1.0f, 1.0f);
    }
    
    private void triggerLightning(World world) {
        List<Player> players = world.getPlayers();
        if (players.isEmpty()) return;
        
        Player target = players.get(random.nextInt(players.size()));
        world.strikeLightning(target.getLocation());
    }
    
    private void triggerFogEffect(World world) {
        for (Player player : world.getPlayers()) {
            player.sendTitle("", "Ein dichter Nebel zieht auf...", 10, 100, 10);
        }
        world.setStorm(true);
        
        new BukkitRunnable() {
            @Override
            public void run() {
                world.setStorm(false);
            }
        }.runTaskLater(arena.getPlugin(), 200L);
    }
}
