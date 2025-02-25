package com.ubivismedia.arenaplugin.leaderboard;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import java.util.*;

public class ArenaLeaderboardManager {
    
    private final Map<UUID, Integer> playerScores = new HashMap<>();
    
    public void addScore(Player player, int score) {
        playerScores.put(player.getUniqueId(), getScore(player) + score);
    }
    
    public int getScore(Player player) {
        return playerScores.getOrDefault(player.getUniqueId(), 0);
    }
    
    public void resetScores() {
        playerScores.clear();
        Bukkit.broadcastMessage("Alle Arena-Scores wurden zurückgesetzt!");
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
}
