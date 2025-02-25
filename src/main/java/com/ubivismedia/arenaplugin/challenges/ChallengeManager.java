package com.ubivismedia.arenaplugin.challenges;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class ChallengeManager {
    
    private final Map<UUID, Integer> playerProgress = new HashMap<>();
    private FileConfiguration config;
    private File configFile;
    private String currentChallenge;
    private int challengeGoal;
    private String challengeType;
    private String entityType;
    private String rewardType;
    private int rewardAmount;
    private int resetTime; // Sekunden bis zur nächsten Challenge
    private final Random random = new Random();
    
    public ChallengeManager() {
        loadConfig();
        generateNewChallenge();
        startChallengeTimer();
    }
    
    private void loadConfig() {
        configFile = new File(Bukkit.getPluginManager().getPlugin("ArenaInstance").getDataFolder(), "challenges.yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }
    
    private void generateNewChallenge() {
        List<String> challengeKeys = new ArrayList<>(config.getConfigurationSection("challenges").getKeys(false));
        if (challengeKeys.isEmpty()) return;
        
        String challengeKey = challengeKeys.get(random.nextInt(challengeKeys.size()));
        currentChallenge = config.getString("challenges." + challengeKey + ".description");
        challengeGoal = config.getInt("challenges." + challengeKey + ".amount");
        challengeType = config.getString("challenges." + challengeKey + ".type");
        entityType = config.getString("challenges." + challengeKey + ".entity", "ALL");
        rewardType = config.getString("challenges." + challengeKey + ".reward.type");
        rewardAmount = config.getInt("challenges." + challengeKey + ".reward.amount");
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
    
    public void recordProgress(Player player, String entity, int value) {
        if (!entityType.equalsIgnoreCase("ALL") && !entityType.equalsIgnoreCase(entity)) {
            return; // Falls die Challenge nur für einen bestimmten Entity-Typ gilt
        }
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
        player.sendMessage("Du erhältst " + rewardAmount + " " + rewardType + " für deinen Erfolg!");
        // Hier könnte ein Belohnungssystem implementiert werden
    }
}
