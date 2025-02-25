package com.ubivismedia.arenaplugin.commands;

import com.ubivismedia.arenaplugin.arena.ArenaManager;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.EntityType;

public class ArenaCommand implements CommandExecutor {
    
    private final ArenaManager arenaManager;
    private final Map<Player, Location> playerLocations = new HashMap<>();
    private final Map<Player, ItemStack[]> savedInventories = new HashMap<>();
    private final Map<Player, ItemStack[]> specialBlocks = new HashMap<>();
    
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
        
        if (args.length < 2 && !args[0].equalsIgnoreCase("leave") && !args[0].equalsIgnoreCase("edit")) {
            player.sendMessage("Benutzung: /arena <create|delete|join|leave|start|edit> <ArenaName>");
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
                if (savedInventories.containsKey(player)) {
                    player.getInventory().setContents(savedInventories.remove(player));
                }
                player.sendMessage("Du hast die Arena verlassen!");
                break;
                
            case "edit":
                if (!arenaManager.arenaExists(arenaName)) {
                    player.sendMessage("Diese Arena existiert nicht!");
                    return true;
                }
                playerLocations.put(player, player.getLocation());
                savedInventories.put(player, player.getInventory().getContents());
                player.teleport(arenaManager.getArena(arenaName).getSpawnLocation());
                player.setGameMode(GameMode.CREATIVE);
                
                ItemStack[] specialItems = new ItemStack[]{
                    new ItemStack(Material.GOLD_BLOCK, 1), // Beispiel für Startblock
                    new ItemStack(Material.EMERALD_BLOCK, 1), // Beispiel für Wellenblock
                    new ItemStack(Material.CHEST, 1), // Beispiel für Waffenkiste
                    new ItemStack(Material.DIAMOND_BLOCK, 1) // Beispiel für Zuschauerblock
                };
                
                specialBlocks.put(player, specialItems);
                player.getInventory().setContents(specialItems);
                
                player.sendMessage("Du bearbeitest nun die Arena " + arenaName + "! Spezielle Blöcke wurden deinem Inventar hinzugefügt.");
                break;
                
            default:
                player.sendMessage("Unbekannter Arena-Befehl! Nutze /arena <create|delete|join|leave|start|edit> <ArenaName>");
                break;
        }
        
        return true;
    }
}
