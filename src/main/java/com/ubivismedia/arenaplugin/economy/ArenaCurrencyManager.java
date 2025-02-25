package com.ubivismedia.arenaplugin.economy;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ArenaCurrencyManager {
    
    private final Map<UUID, Integer> playerBalances = new HashMap<>();
    
    public void addCurrency(Player player, int amount) {
        playerBalances.put(player.getUniqueId(), getBalance(player) + amount);
        player.sendMessage("Du hast " + amount + " Arena-Diamanten erhalten! Neuer Kontostand: " + getBalance(player));
    }
    
    public void removeCurrency(Player player, int amount) {
        int currentBalance = getBalance(player);
        if (currentBalance >= amount) {
            playerBalances.put(player.getUniqueId(), currentBalance - amount);
            player.sendMessage("Du hast " + amount + " Arena-Diamanten ausgegeben! Neuer Kontostand: " + getBalance(player));
        } else {
            player.sendMessage("Nicht genügend Arena-Diamanten!");
        }
    }
    
    public int getBalance(Player player) {
        return playerBalances.getOrDefault(player.getUniqueId(), 0);
    }
    
    public void resetBalance(Player player) {
        playerBalances.put(player.getUniqueId(), 0);
        player.sendMessage("Dein Arena-Diamanten-Kontostand wurde zurückgesetzt.");
    }
}
