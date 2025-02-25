package com.ubivismedia.arenaplugin.challenges;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Random;

public class ChallengeManager {
    
    private final Map<UUID, Integer> playerProgress = new HashMap<>();
    private String currentChallenge;
    private int challengeGoal;
    private int resetTime; // Sekunden bis zur nächsten Challenge
    private final Random random = new Random();
    
    public ChallengeManager() {
        generateNewChallenge();
        startChallengeTimer();
    }
    
    private void generateNewChallenge() {
        String[] challenges = {
            "Besiege 50 Gegner in einer Arena",
            "Überlebe 10 Wellen ohne Schaden zu nehmen",
            "Töte 3 Mitspieler in PvP-Modus",
            "Schließe eine Arena mit mindestens 500 Punkten ab"
        };
        
        int[] goals = {50, 10, 3, 500};
        
        int index = random.nextInt(challenges.length);
        currentChallenge = challenges[index];
        challengeGoal = goals[index];
        resetTime = 86400; // 24 Stunden (in Sekunden)
        
        Bukkit.broadcastMessage("Neue tägliche Herausforderung: " + currentChallenge);
    }
    
    private void startChallengeTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                resetTime -= 60; // Reduziert um 1 Minute
                if (resetTime <= 0) {
                    generateNewChallenge();
                }
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("ArenaInstance"), 0L, 1200L); // Jede Minute prüfen
    }
    
    public void recordProgress(Player player, int value) {
        playerProgress.put(player.getUniqueId(), playerProgress.getOrDefault(player.getUniqueId(), 0) + value);
        checkCompletion(player);
    }
    
    private void checkCompletion(Player player) {
        if (playerProgress.getOrDefault(player.getUniqueId(), 0) >= challengeGoal) {
            player.sendMessage("Du hast die Herausforderung abgeschlossen: " + currentChallenge);
            rewardPlayer(player);
            playerProgress.put(player.getUniqueId(), 0); // Zurücksetzen für nächste Challenge
        }
    }
    
    private void rewardPlayer(Player player) {
        player.sendMessage("Du erhältst Arena-Diamanten für deinen Erfolg!");
        // Hier könnte ein Belohnungssystem implementiert werden
    }
}
