package com.ubivismedia.arenaplugin.arena;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ArenaSpectatorManager implements CommandExecutor {
    
    private final Map<UUID, String> spectators = new HashMap<>();
    private final Map<UUID, ArmorStand> spectatorAvatars = new HashMap<>();
    
    public void addSpectator(Player player, String arenaName, Location spectatorBlockLocation) {
        spectators.put(player.getUniqueId(), arenaName);
        player.setGameMode(GameMode.SPECTATOR);
        player.sendMessage("Du beobachtest nun die Arena " + arenaName + "!");
        
        // Erstelle einen Zuschauer-Avatar auf dem Zuschauerblock
        ArmorStand stand = spectatorBlockLocation.getWorld().spawn(spectatorBlockLocation, ArmorStand.class);
        stand.setCustomName(player.getName());
        stand.setCustomNameVisible(true);
        stand.setInvisible(false);
        stand.setGravity(false);
        stand.setInvulnerable(true);
        stand.setMarker(true);
        stand.setCollidable(false);
        
        // Setze den Kopf des ArmorStand auf den Spieler-Skin
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
        if (meta != null) {
            meta.setOwningPlayer(player);
            playerHead.setItemMeta(meta);
        }
        stand.getEquipment().setHelmet(playerHead);
        
        spectatorAvatars.put(player.getUniqueId(), stand);
    }
    
    public void removeSpectator(Player player) {
        if (spectators.containsKey(player.getUniqueId())) {
            spectators.remove(player.getUniqueId());
            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage("Du hast den Zuschauer-Modus verlassen.");
            
            // Entferne den Zuschauer-Avatar
            if (spectatorAvatars.containsKey(player.getUniqueId())) {
                spectatorAvatars.get(player.getUniqueId()).remove();
                spectatorAvatars.remove(player.getUniqueId());
            }
        }
    }
    
    public boolean isSpectating(Player player) {
        return spectators.containsKey(player.getUniqueId());
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Nur Spieler können diesen Befehl ausführen!");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length < 1) {
            player.sendMessage("Benutzung: /arena view <ArenaName>");
            return true;
        }
        
        String arenaName = args[0];
        Location spectatorBlockLocation = getSpectatorBlockLocation(arenaName);
        
        if (spectatorBlockLocation == null) {
            player.sendMessage("Diese Arena existiert nicht oder hat keinen Zuschauerbereich!");
            return true;
        }
        
        addSpectator(player, arenaName, spectatorBlockLocation);
        return true;
    }
    
    private Location getSpectatorBlockLocation(String arenaName) {
        // Diese Methode sollte die Position eines Zuschauerblocks aus der Arenadatenbank oder Konfiguration abrufen.
        return null; // Platzhalter
    }
}
