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
import org.bukkit.scheduler.BukkitRunnable;
import java.util.*;

public class ArenaSpectatorManager implements CommandExecutor {
    
    private final Map<UUID, String> spectators = new HashMap<>();
    private final Map<UUID, ArmorStand> spectatorAvatars = new HashMap<>();
    private final Random random = new Random();
    private final Map<String, List<Location>> arenaSpectatorBlocks = new HashMap<>();
    
    public ArenaSpectatorManager() {
        startSpectatorAnimations();
    }
    
    public void addSpectator(Player player, String arenaName) {
        spectators.put(player.getUniqueId(), arenaName);
        player.setGameMode(GameMode.SPECTATOR);
        player.sendMessage("Du beobachtest nun die Arena " + arenaName + "!");
        
        // Wähle einen zufälligen Zuschauerblock
        Location spectatorBlockLocation = getRandomSpectatorBlockLocation(arenaName);
        if (spectatorBlockLocation == null) {
            player.sendMessage("Kein freier Zuschauerplatz verfügbar!");
            return;
        }
        
        // Erstelle einen Zuschauer-Avatar auf dem ausgewählten Block
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
    
    private void startSpectatorAnimations() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (ArmorStand stand : spectatorAvatars.values()) {
                    int action = random.nextInt(3);
                    switch (action) {
                        case 0: // Kopf leicht drehen
                            stand.setHeadPose(stand.getHeadPose().setY(stand.getHeadPose().getY() + 0.1));
                            break;
                        case 1: // Jubeln (kurz nach oben bewegen)
                            stand.teleport(stand.getLocation().add(0, 0.2, 0));
                            Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("ArenaInstance"), () -> 
                                stand.teleport(stand.getLocation().add(0, -0.2, 0)), 10L);
                            break;
                        case 2: // Winken (Arme hoch und runter bewegen)
                            stand.setArms(true);
                            Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("ArenaInstance"), () ->
                                stand.setArms(false), 10L);
                            break;
                    }
                }
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("ArenaInstance"), 0L, 100L);
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
        addSpectator(player, arenaName);
        return true;
    }
    
    private Location getRandomSpectatorBlockLocation(String arenaName) {
        List<Location> spectatorBlocks = arenaSpectatorBlocks.getOrDefault(arenaName, new ArrayList<>());
        if (spectatorBlocks.isEmpty()) {
            return null;
        }
        return spectatorBlocks.get(random.nextInt(spectatorBlocks.size()));
    }
    
    public void addSpectatorBlock(String arenaName, Location location) {
        arenaSpectatorBlocks.computeIfAbsent(arenaName, k -> new ArrayList<>()).add(location);
    }
    
    public void removeSpectatorBlock(String arenaName, Location location) {
        List<Location> blocks = arenaSpectatorBlocks.get(arenaName);
        if (blocks != null) {
            blocks.remove(location);
        }
    }
}
