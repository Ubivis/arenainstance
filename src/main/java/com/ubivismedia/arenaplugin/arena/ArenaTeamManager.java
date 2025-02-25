package com.ubivismedia.arenaplugin.arena;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.*;

public class ArenaTeamManager {
    
    private final Map<String, List<UUID>> arenaTeams = new HashMap<>();
    private final Map<UUID, String> playerTeams = new HashMap<>();
    
    public void addPlayerToTeam(String arenaName, Player player) {
        arenaTeams.putIfAbsent(arenaName, new ArrayList<>());
        arenaTeams.get(arenaName).add(player.getUniqueId());
        playerTeams.put(player.getUniqueId(), arenaName);
        player.sendMessage("Du bist jetzt Teil eines Teams in Arena " + arenaName + "!");
    }
    
    public void removePlayerFromTeam(Player player) {
        String arenaName = playerTeams.get(player.getUniqueId());
        if (arenaName != null) {
            arenaTeams.get(arenaName).remove(player.getUniqueId());
            playerTeams.remove(player.getUniqueId());
            player.sendMessage("Du hast das Team in Arena " + arenaName + " verlassen.");
        }
    }
    
    public boolean isPlayerInTeam(Player player) {
        return playerTeams.containsKey(player.getUniqueId());
    }
    
    public List<UUID> getTeamMembers(String arenaName) {
        return arenaTeams.getOrDefault(arenaName, new ArrayList<>());
    }
    
    public void shareRewardsAmongTeam(String arenaName, int totalReward) {
        List<UUID> teamMembers = arenaTeams.getOrDefault(arenaName, new ArrayList<>());
        if (teamMembers.isEmpty()) return;
        
        int rewardPerPlayer = totalReward / teamMembers.size();
        for (UUID playerId : teamMembers) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null) {
                player.sendMessage("Du erhältst " + rewardPerPlayer + " Arena-Diamanten für den Teamerfolg!");
                // Hier könnte die Belohnung ins Inventar gelegt werden
            }
        }
    }
}
