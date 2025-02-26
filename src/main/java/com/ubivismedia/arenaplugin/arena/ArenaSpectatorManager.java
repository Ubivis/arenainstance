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

public class ArenaEventManager {
    
    private final Arena arena;
    private final Random random = new Random();
    private final ArenaCurrencyManager currencyManager;
    
    public ArenaEventManager(Arena arena, ArenaCurrencyManager currencyManager) {
        this.arena = arena;
        this.currencyManager = currencyManager;
    }
    
    public void handleWaveStart(int waveNumber, World world) {
        if (waveNumber % 2 == 0) { // Nur jede zweite Welle
            triggerRandomEvent(world, waveNumber);
        }
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
    }
    
    private void applyCurseEffect(World world, int waveNumber) {
        List<Player> players = world.getPlayers();
        if (players.isEmpty()) return;
        
        for (Player player : players) {
            player.sendMessage("Ein dunkler Fluch wurde auf die Arena gelegt für Welle " + waveNumber + "!");
            world.playSound(player.getLocation(), Sound.ENTITY_WITCH_AMBIENT, 1.0f, 1.0f);
        }
    }
    
    private void triggerFogEffect(World world, int waveNumber) {
        for (Player player : world.getPlayers()) {
            player.sendTitle("", "Dichter Nebel zieht auf für Welle " + waveNumber + "...", 10, 100, 10);
        }
        world.setStorm(true);
    }
    
    public void handleArenaEnd(World world, boolean won, int wavesCompleted, int mobsKilled, int playerKills) {
        List<Player> players = world.getPlayers();
        int totalReward = calculateReward(won, wavesCompleted, mobsKilled, playerKills);
        
        for (Player player : players) {
            currencyManager.addCurrency(player, totalReward);
            player.sendMessage("Du hast " + totalReward + " Arena-Diamanten erhalten!");
        }
    }
    
    private int calculateReward(boolean won, int wavesCompleted, int mobsKilled, int playerKills) {
        int baseReward = won ? 50 : 20; // Gewinner erhalten mehr
        int waveBonus = wavesCompleted * 5; // 5 Diamanten pro abgeschlossene Welle
        int mobBonus = mobsKilled * 2; // 2 Diamanten pro besiegtem Mob
        int pvpBonus = playerKills * 10; // 10 Diamanten pro getötetem Spieler
        
        return baseReward + waveBonus + mobBonus + pvpBonus;
    }
    
    // Spectator-Features
    public void enableSpectatorMode(Player player) {
        player.setGameMode(org.bukkit.GameMode.SPECTATOR);
        player.sendMessage("Du bist nun Zuschauer in der Arena!");
    }
    
    public void switchSpectatorTarget(Player player) {
        List<Player> alivePlayers = arena.getAlivePlayers();
        if (alivePlayers.isEmpty()) {
            player.sendMessage("Keine Spieler zum Zuschauen verfügbar.");
            return;
        }
        
        Player target = alivePlayers.get(random.nextInt(alivePlayers.size()));
        player.setSpectatorTarget(target);
        player.sendMessage("Du beobachtest nun " + target.getName() + "!");
    }
    
    public void exitSpectatorMode(Player player) {
        player.setGameMode(org.bukkit.GameMode.ADVENTURE);
        player.sendMessage("Du hast den Zuschauermodus verlassen!");
    }
}
