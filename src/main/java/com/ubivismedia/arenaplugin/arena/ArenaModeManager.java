package com.ubivismedia.arenaplugin.arena;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ArenaModeManager {
    
    public enum ArenaMode {
        SURVIVAL,
        BOSS,
        SPEEDRUN
    }
    
    private final Map<UUID, ArenaMode> playerModes = new HashMap<>();
    
    public void setPlayerMode(Player player, ArenaMode mode) {
        playerModes.put(player.getUniqueId(), mode);
        player.sendMessage("Du spielst jetzt im " + mode.name() + "-Modus!");
    }
    
    public ArenaMode getPlayerMode(Player player) {
        return playerModes.getOrDefault(player.getUniqueId(), ArenaMode.SURVIVAL);
    }
    
    public void applyModeEffects(Player player) {
        ArenaMode mode = getPlayerMode(player);
        
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
