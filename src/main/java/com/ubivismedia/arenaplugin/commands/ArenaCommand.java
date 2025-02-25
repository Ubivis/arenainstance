package com.ubivismedia.arenaplugin.commands;

import com.ubivismedia.arenaplugin.arena.ArenaManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArenaCommand implements CommandExecutor {
    
    private final ArenaManager arenaManager;
    
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
        
        if (args.length < 2) {
            player.sendMessage("Benutzung: /arena create <ArenaName>");
            return true;
        }
        
        if (args[0].equalsIgnoreCase("create")) {
            String arenaName = args[1];
            if (arenaManager.arenaExists(arenaName)) {
                player.sendMessage("Eine Arena mit diesem Namen existiert bereits!");
                return true;
            }
            
            arenaManager.createArena(arenaName);
            player.sendMessage("Arena " + arenaName + " wurde erfolgreich erstellt!");
            return true;
        }
        
        player.sendMessage("Unbekannter Arena-Befehl!");
        return true;
    }
}
