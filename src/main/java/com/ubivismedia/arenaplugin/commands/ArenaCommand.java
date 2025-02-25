package com.ubivismedia.arenaplugin.arena;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ArenaManager {
    
    private final Map<String, Arena> arenas = new HashMap<>();
    private final File arenaFile;
    private final FileConfiguration arenaConfig;
    
    public ArenaManager() {
        arenaFile = new File("plugins/ArenaInstance/arenas.yml");
        if (!arenaFile.exists()) {
            try {
                arenaFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        arenaConfig = YamlConfiguration.loadConfiguration(arenaFile);
        loadArenas();
    }
    
    public void createArena(String name) {
        if (arenas.containsKey(name)) {
            throw new IllegalArgumentException("Eine Arena mit diesem Namen existiert bereits!");
        }
        Arena arena = new Arena(name);
        arenas.put(name, arena);
        saveArenas();
    }
    
    public void removeArena(String name) {
        if (arenas.containsKey(name)) {
            arenas.remove(name);
            arenaConfig.set("arenas." + name, null);
            saveArenas();
        }
    }
    
    public boolean arenaExists(String name) {
        return arenas.containsKey(name);
    }
    
    public Arena getArena(String name) {
        return arenas.get(name);
    }
    
    public void saveArenas() {
        for (String name : arenas.keySet()) {
            arenaConfig.set("arenas." + name + ".world", name);
            arenaConfig.set("arenas." + name + ".wave.amount", 5); // Standard-Wellenanzahl
            arenaConfig.set("arenas." + name + ".wave.interval", 10); // Standard-Intervall in Sekunden
            arenaConfig.set("arenas." + name + ".wave.mob", "ZOMBIE"); // Standardgegner
        }
        try {
            arenaConfig.save(arenaFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void loadArenas() {
        if (arenaConfig.contains("arenas")) {
            for (String name : arenaConfig.getConfigurationSection("arenas").getKeys(false)) {
                World world = Bukkit.getWorld(name);
                if (world != null) {
                    Arena arena = new Arena(name);
                    // Laden der Wellenkonfiguration
                    int amount = arenaConfig.getInt("arenas." + name + ".wave.amount", 5);
                    int interval = arenaConfig.getInt("arenas." + name + ".wave.interval", 10);
                    String mob = arenaConfig.getString("arenas." + name + ".wave.mob", "ZOMBIE");
                    arena.setWaveConfiguration(amount, interval, mob);
                    arenas.put(name, arena);
                }
            }
        }
    }
}
