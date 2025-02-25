package com.ubivismedia.arenaplugin.arena;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;

public class ArenaPvPManager {
    
    private final Map<String, Boolean> arenaPvPSettings = new HashMap<>();
    private final Map<Player, Integer> playerKills = new HashMap<>();
    
    public void setPvPEnabled(String arenaName, boolean enabled) {
        arenaPvPSettings.put(arenaName, enabled);
        Bukkit.broadcastMessage("PvP in der Arena " + arenaName + " ist jetzt " + (enabled ? "aktiviert" : "deaktiviert") + "!");
    }
    
    public boolean isPvPEnabled(String arenaName) {
        return arenaPvPSettings.getOrDefault(arenaName, false);
    }
    
    public void recordKill(Player killer) {
        playerKills.put(killer, playerKills.getOrDefault(killer, 0) + 1);
    }
    
    public int getPlayerKills(Player player) {
        return playerKills.getOrDefault(player, 0);
    }
}
