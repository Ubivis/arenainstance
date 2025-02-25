package com.ubivismedia.arenaplugin.arena;

import com.ubivismedia.arenaplugin.economy.ArenaCurrencyManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.List;
import java.util.Random;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ArenaEventManager {
    
    private final Arena arena;
    private final Random random = new Random();
    private final ArenaCurrencyManager currencyManager;
    private final File logFile = new File("plugins/ArenaInstance/arena_logs.txt");
    
    public ArenaEventManager(Arena arena, ArenaCurrencyManager currencyManager) {
        this.arena = arena;
        this.currencyManager = currencyManager;
    }
    
    public void handleWaveStart(int waveNumber, World world) {
        if (waveNumber % 2 == 0) { // Nur jede zweite Welle
            triggerRandomEvent(world, waveNumber);
        }
        if (waveNumber % 5 == 0) {
            triggerSpecialWave(world, waveNumber);
        }
        logEvent("Welle " + waveNumber + " gestartet in Arena " + arena.getName());
    }

    private void triggerSpecialWave(World world, int waveNumber) {
        int eventType = random.nextInt(3);
        switch (eventType) {
            case 0:
                spawnBossWave(world, waveNumber);
                break;
            case 1:
                triggerChaosWave(world, waveNumber);
                break;
            case 2:
                applyExplosionWave(world, waveNumber);
                break;
        }
    }

    private void spawnBossWave(World world, int waveNumber) {
        Location bossSpawn = world.getSpawnLocation().add(0, 1, 0);
        Zombie boss = world.spawn(bossSpawn, Zombie.class);
        boss.setCustomName("§cBoss-Welle " + waveNumber);
        boss.setCustomNameVisible(true);
        boss.setHealth(50.0);
        
        Bukkit.broadcastMessage("§cBoss-Welle " + waveNumber + " hat begonnen!");
        logEvent("Boss-Welle in Arena " + arena.getName() + " gestartet (Welle " + waveNumber + ")");
    }
    
    private void triggerChaosWave(World world, int waveNumber) {
        Bukkit.broadcastMessage("§4Chaos-Welle! Doppelte Gegner erscheinen!");
        logEvent("Chaos-Welle in Arena " + arena.getName() + " (Welle " + waveNumber + ")");
    }
    
    private void applyExplosionWave(World world, int waveNumber) {
        Bukkit.broadcastMessage("§6Explosions-Welle! Alle Gegner explodieren beim Tod!");
        logEvent("Explosions-Welle in Arena " + arena.getName() + " (Welle " + waveNumber + ")");
    }
    
    private void triggerRandomEvent(World world, int waveNumber) {
        int eventType = random.nextInt(3);
        switch (eventType) {
            case 0:
                spawnMysteryMerchant(world, waveNumber);
                break;
            case 1:
                applyCurseEffect(world, waveNumber);
                break;
            case 2:
                triggerFogEffect(world, waveNumber);
                break;
        }
    }
    
    private void spawnMysteryMerchant(World world, int waveNumber) {
        List<Player> players = world.getPlayers();
        if (players.isEmpty()) return;
        
        Player target = players.get(random.nextInt(players.size()));
        Location loc = target.getLocation().add(2, 0, 2);
        
        Villager merchant = world.spawn(loc, Villager.class);
        merchant.setCustomName("Mysteriöser Händler - Welle " + waveNumber);
        merchant.setCustomNameVisible(true);
        merchant.setMetadata("arena_merchant", new FixedMetadataValue(arena.getPlugin(), true));
        
        target.sendMessage("Ein mysteriöser Händler ist erschienen für Welle " + waveNumber + "!");
        logEvent("Mysteriöser Händler in Arena " + arena.getName() + " bei Welle " + waveNumber);
    }
    
    private void applyCurseEffect(World world, int waveNumber) {
        List<Player> players = world.getPlayers();
        if (players.isEmpty()) return;
        
        for (Player player : players) {
            player.sendMessage("Ein dunkler Fluch wurde auf die Arena gelegt für Welle " + waveNumber + "!");
            world.playSound(player.getLocation(), Sound.ENTITY_WITCH_AMBIENT, 1.0f, 1.0f);
        }
        logEvent("Fluch-Effekt auf Arena " + arena.getName() + " bei Welle " + waveNumber);
    }
    
    private void triggerFogEffect(World world, int waveNumber) {
        for (Player player : world.getPlayers()) {
            player.sendTitle("", "Dichter Nebel zieht auf für Welle " + waveNumber + "...", 10, 100, 10);
        }
        world.setStorm(true);
        logEvent("Nebel-Effekt in Arena " + arena.getName() + " bei Welle " + waveNumber);
    }
    
    public void handleArenaEnd(World world, boolean won, int wavesCompleted, int mobsKilled, int playerKills) {
        List<Player> players = world.getPlayers();
        int totalReward = calculateReward(won, wavesCompleted, mobsKilled, playerKills);
        
        for (Player player : players) {
            currencyManager.addCurrency(player, totalReward);
            player.sendMessage("Du hast " + totalReward + " Arena-Diamanten erhalten!");
        }
        logEvent("Arena " + arena.getName() + " beendet. Ergebnis: " + (won ? "Gewonnen" : "Verloren") + ", Wellen: " + wavesCompleted + ", Mobs besiegt: " + mobsKilled + ", PvP-Kills: " + playerKills);
    }
    
    private int calculateReward(boolean won, int wavesCompleted, int mobsKilled, int playerKills) {
        int baseReward = won ? 50 : 20; // Gewinner erhalten mehr
        int waveBonus = wavesCompleted * 5; // 5 Diamanten pro abgeschlossene Welle
        int mobBonus = mobsKilled * 2; // 2 Diamanten pro besiegtem Mob
        int pvpBonus = playerKills * 10; // 10 Diamanten pro getötetem Spieler
        
        return baseReward + waveBonus + mobBonus + pvpBonus;
    }
    
    private void logEvent(String message) {
        try (FileWriter writer = new FileWriter(logFile, true)) {
            writer.write("[ArenaEvent] " + message + "\n");
        } catch (IOException e) {
            Bukkit.getLogger().warning("Fehler beim Schreiben ins Log: " + e.getMessage());
        }
    }
}
