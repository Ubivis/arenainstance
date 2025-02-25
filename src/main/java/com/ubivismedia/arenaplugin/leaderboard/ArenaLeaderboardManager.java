package com.ubivismedia.arenaplugin.leaderboard;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.scoreboard.*;
import java.util.*;

public class ArenaLeaderboardManager {
    
    private final Map<UUID, Integer> playerScores = new HashMap<>();
    private Location leaderboardLocation;
    private Scoreboard scoreboard;
    private Objective objective;
    private final List<Hologram> holograms = new ArrayList<>();
    
    public ArenaLeaderboardManager() {
        setupScoreboard();
    }
    
    private void setupScoreboard() {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective("arenaScores", "dummy", "§6Arena Scores");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }
    
    public void addScore(Player player, int score) {
        playerScores.put(player.getUniqueId(), getScore(player) + score);
        updateHUDScoreboard();
        updateHologramDisplay();
    }
    
    public int getScore(Player player) {
        return playerScores.getOrDefault(player.getUniqueId(), 0);
    }
    
    public void resetScores() {
        playerScores.clear();
        Bukkit.broadcastMessage("Alle Arena-Scores wurden zurückgesetzt!");
        updateHUDScoreboard();
        updateLeaderboardDisplay();
        updateHologramDisplay();
    }
    
    public List<Map.Entry<UUID, Integer>> getTopPlayers(int limit) {
        List<Map.Entry<UUID, Integer>> sortedScores = new ArrayList<>(playerScores.entrySet());
        sortedScores.sort((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()));
        return sortedScores.subList(0, Math.min(limit, sortedScores.size()));
    }
    
    public void displayLeaderboard(Player player) {
        player.sendMessage("§6--- Arena Highscores ---");
        List<Map.Entry<UUID, Integer>> topPlayers = getTopPlayers(10);
        int rank = 1;
        for (Map.Entry<UUID, Integer> entry : topPlayers) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(entry.getKey());
            player.sendMessage("§e" + rank + ". " + offlinePlayer.getName() + " - " + entry.getValue() + " Punkte");
            rank++;
        }
    }
    
    public void setLeaderboardLocation(Location location) {
        this.leaderboardLocation = location;
    }
    
    public void updateLeaderboardDisplay() {
        if (leaderboardLocation == null || !(leaderboardLocation.getBlock().getState() instanceof Sign)) {
            return;
        }
        
        Sign sign = (Sign) leaderboardLocation.getBlock().getState();
        List<Map.Entry<UUID, Integer>> topPlayers = getTopPlayers(4);
        
        sign.setLine(0, "§6Arena Highscores");
        for (int i = 0; i < topPlayers.size(); i++) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(topPlayers.get(i).getKey());
            sign.setLine(i + 1, "§e" + (i + 1) + ". " + offlinePlayer.getName());
        }
        sign.update();
    }
    
    public void updateHUDScoreboard() {
        objective.getScoreboard().getEntries().forEach(objective.getScoreboard()::resetScores);
        List<Map.Entry<UUID, Integer>> topPlayers = getTopPlayers(5);
        int rank = 1;
        for (Map.Entry<UUID, Integer> entry : topPlayers) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(entry.getKey());
            Score score = objective.getScore("§e" + rank + ". " + offlinePlayer.getName());
            score.setScore(entry.getValue());
            rank++;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(scoreboard);
        }
    }
    
    public void displayEndScreenScores() {
        Bukkit.broadcastMessage("§6--- Endstand der Arena ---");
        List<Map.Entry<UUID, Integer>> topPlayers = getTopPlayers(10);
        int rank = 1;
        for (Map.Entry<UUID, Integer> entry : topPlayers) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(entry.getKey());
            Bukkit.broadcastMessage("§e" + rank + ". " + offlinePlayer.getName() + " - " + entry.getValue() + " Punkte");
            rank++;
        }
    }
    
    public void updateHologramDisplay() {
        clearExistingHolograms();
        List<Map.Entry<UUID, Integer>> topPlayers = getTopPlayers(5);
        
        Location baseLocation = leaderboardLocation.clone().add(0, 5, 0); // Start above the leaderboard
        for (int i = 0; i < topPlayers.size(); i++) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(topPlayers.get(i).getKey());
            String text = "§e" + (i + 1) + ". " + offlinePlayer.getName() + " - " + topPlayers.get(i).getValue() + " Punkte";
            createHologram(baseLocation.clone().add(0, -i * 0.3, 0), text);
        }
    }
    
    private void clearExistingHolograms() {
        for (Hologram hologram : holograms) {
            hologram.remove();
        }
        holograms.clear();
    }
    
    private void createHologram(Location location, String text) {
        Hologram hologram = new Hologram(location, text);
        holograms.add(hologram);
        hologram.spawn();
    }

    // Hologram class is a placeholder for your actual hologram implementation
    private static class Hologram {
        private final Location location;
        private final String text;
        
        public Hologram(Location location, String text) {
            this.location = location;
            this.text = text;
        }
        
        public void spawn() {
            // Spawn hologram logic here
        }
        
        public void remove() {
            // Remove hologram logic here
        }
    }
}
