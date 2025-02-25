package com.ubivismedia.arenaplugin.commands;

import com.ubivismedia.arenaplugin.arena.ArenaManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;

public class ArenaCommand implements CommandExecutor {
    
    private final ArenaManager arenaManager;
    private final Map<Player, Location> playerLocations = new HashMap<>();
    
    public ArenaCommand(ArenaManager arenaManager) {
        this.arenaManager = arenaManager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Nur Spieler können diesen Befehl ausführen!");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length < 2 && !args[0].equalsIgnoreCase("leave")) {
            player.sendMessage("Benutzung: /arena <create|delete|join|leave> <ArenaName>");
            return true;
        }
        
        String action = args[0].toLowerCase();
        String arenaName = args.length > 1 ? args[1] : "";

        switch (action) {
            case "create":
                if (arenaManager.arenaExists(arenaName)) {
                    player.sendMessage("Eine Arena mit diesem Namen existiert bereits!");
                    return true;
                }
                arenaManager.createArena(arenaName);
                player.sendMessage("Arena " + arenaName + " wurde erfolgreich erstellt!");
                break;
            
            case "delete":
                if (!arenaManager.arenaExists(arenaName)) {
                    player.sendMessage("Diese Arena existiert nicht!");
                    return true;
                }
                arenaManager.removeArena(arenaName);
                player.sendMessage("Arena " + arenaName + " wurde gelöscht!");
                break;
                
            case "join":
                if (!arenaManager.arenaExists(arenaName)) {
                    player.sendMessage("Diese Arena existiert nicht!");
                    return true;
                }
                playerLocations.put(player, player.getLocation());
                player.teleport(arenaManager.getArena(arenaName).getSpawnLocation());
                player.sendMessage("Du bist der Arena " + arenaName + " beigetreten!");
                break;
            
            case "leave":
                if (!playerLocations.containsKey(player)) {
                    player.sendMessage("Du bist derzeit in keiner Arena!");
                    return true;
                }
                player.teleport(playerLocations.remove(player));
                player.sendMessage("Du hast die Arena verlassen!");
                break;
                
            default:
                player.sendMessage("Unbekannter Arena-Befehl! Nutze /arena <create|delete|join|leave> <ArenaName>");
                break;
        }
        
        return true;
    }
}
