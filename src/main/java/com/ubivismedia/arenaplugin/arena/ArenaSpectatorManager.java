package com.ubivismedia.arenaplugin.arena;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ArenaSpectatorManager {
    
    private final Map<UUID, String> spectators = new HashMap<>();
    
    public void addSpectator(Player player, String arenaName) {
        spectators.put(player.getUniqueId(), arenaName);
        player.setGameMode(GameMode.SPECTATOR);
        player.sendMessage("Du beobachtest nun die Arena " + arenaName + "!");
    }
    
    public void removeSpectator(Player player) {
        if (spectators.containsKey(player.getUniqueId())) {
            spectators.remove(player.getUniqueId());
            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage("Du hast den Zuschauer-Modus verlassen.");
        }
    }
    
    public boolean isSpectating(Player player) {
        return spectators.containsKey(player.getUniqueId());
    }
}
