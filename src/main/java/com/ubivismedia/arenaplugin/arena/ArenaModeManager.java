package com.ubivismedia.arenaplugin.arena;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;

public class ArenaModeManager {
    
    public enum ArenaMode {
        SURVIVAL,
        BOSS,
        SPEEDRUN
    }
    
    private final Map<String, ArenaMode> arenaModes = new HashMap<>();
    
    public void setArenaMode(String arenaName, ArenaMode mode) {
        arenaModes.put(arenaName, mode);
        Bukkit.broadcastMessage("Die Arena " + arenaName + " ist jetzt im " + mode.name() + "-Modus!");
    }
    
    public ArenaMode getArenaMode(String arenaName) {
        return arenaModes.getOrDefault(arenaName, ArenaMode.SURVIVAL);
    }
    
    public void applyModeEffects(Player player, String arenaName) {
        ArenaMode mode = getArenaMode(arenaName);
        
        switch (mode) {
            case SURVIVAL:
                player.sendMessage("Überlebe so lange wie möglich!");
                break;
            case BOSS:
                player.sendMessage("Besiege den Boss, um die Arena abzuschließen!");
                break;
            case SPEEDRUN:
                player.sendMessage("Schließe die Wellen so schnell wie möglich ab!");
                break;
        }
    }
}
