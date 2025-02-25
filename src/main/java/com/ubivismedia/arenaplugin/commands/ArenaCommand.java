package com.ubivismedia.arenaplugin.commands;

import com.ubivismedia.arenaplugin.arena.ArenaManager;
import com.ubivismedia.arenaplugin.gui.ArenaEditGUI;
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
                
            case "start":
                if (!arenaManager.arenaExists(arenaName)) {
                    player.sendMessage("Diese Arena existiert nicht!");
                    return true;
                }
                
                String instanceName = arenaName + "-" + player.getName();
                World arenaInstance = Bukkit.createWorld(new WorldCreator(instanceName));
                if (arenaInstance == null) {
                    player.sendMessage("Fehler beim Erstellen der Arena-Instanz!");
                    return true;
                }
                
                player.sendMessage("Der Arenakampf startet in 10 Sekunden!");
                Bukkit.getScheduler().runTaskLater(arenaManager.getPlugin(), () -> {
                    playerLocations.put(player, player.getLocation());
                    savedInventories.put(player, player.getInventory().getContents());
                    player.teleport(arenaInstance.getSpawnLocation());
                    player.getInventory().clear();
                    player.getInventory().addItem(new ItemStack(Material.WOODEN_SWORD));
                    player.sendMessage("Der Arenakampf hat begonnen! Erste Gegner erscheinen in 5 Sekunden!");
                    
                    new BukkitRunnable() {
                        private int wave = 1;
                        @Override
                        public void run() {
                            player.sendMessage("Welle " + wave + " beginnt! Gegner erscheinen.");
                            for (int i = 0; i < wave; i++) {
                                arenaInstance.spawnEntity(player.getLocation().add(2, 0, 2), EntityType.ZOMBIE);
                            }
                            wave++;
                        }
                    }.runTaskTimer(arenaManager.getPlugin(), 100L, 200L);
                }, 200L);
                
                break;
                
            case "edit":
                if (!arenaManager.arenaExists(arenaName)) {
                    player.sendMessage("Diese Arena existiert nicht!");
                    return true;
                }
                new ArenaEditGUI(player, arenaManager.getArena(arenaName)).open();
                player.sendMessage("Arena-Editor für " + arenaName + " wurde geöffnet!");
                break;
                
            default:
                player.sendMessage("Unbekannter Arena-Befehl! Nutze /arena <create|delete|join|leave|start|edit> <ArenaName>");
                break;
        }
        
        return true;
    }
}
