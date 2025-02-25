package com.ubivismedia.arenaplugin;

import com.ubivismedia.arenaplugin.arena.ArenaManager;
import com.ubivismedia.arenaplugin.commands.ArenaCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class ArenaInstance extends JavaPlugin {

    private ArenaManager arenaManager;

    @Override
    public void onEnable() {
        getLogger().info("Arena Plugin wurde aktiviert!");
        
        // Initialisierung des ArenaManagers
        arenaManager = new ArenaManager();
        
        // Initialisierung der Befehle
        getCommand("arena").setExecutor(new ArenaCommand(arenaManager));
    }

    @Override
    public void onDisable() {
        getLogger().info("Arena Plugin wurde deaktiviert!");
    }
}
