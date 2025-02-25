package com.ubivismedia.arenaplugin;

import com.ubivismedia.arenaplugin.arena.ArenaManager;
import com.ubivismedia.arenaplugin.commands.ArenaCommand;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.*;

public class ArenaInstance extends JavaPlugin {

    private ArenaManager arenaManager;
    private final File arenasFile = new File(getDataFolder(), "arenas.dat");

    @Override
    public void onEnable() {
        getLogger().info("Arena Plugin wurde aktiviert!");
        
        // Initialisierung des ArenaManagers
        arenaManager = new ArenaManager();
        loadArenas();
        
        // Initialisierung der Befehle
        getCommand("arena").setExecutor(new ArenaCommand(arenaManager));
    }

    @Override
    public void onDisable() {
        getLogger().info("Arena Plugin wurde deaktiviert!");
        saveArenas();
    }

    private void saveArenas() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arenasFile))) {
            oos.writeObject(arenaManager.getArenas());
            getLogger().info("Arenen gespeichert.");
        } catch (IOException e) {
            getLogger().severe("Fehler beim Speichern der Arenen: " + e.getMessage());
        }
    }

    private void loadArenas() {
        if (!arenasFile.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arenasFile))) {
            arenaManager.loadArenas((Map<String, Arena>) ois.readObject());
            getLogger().info("Arenen geladen.");
        } catch (IOException | ClassNotFoundException e) {
            getLogger().severe("Fehler beim Laden der Arenen: " + e.getMessage());
        }
    }
}
