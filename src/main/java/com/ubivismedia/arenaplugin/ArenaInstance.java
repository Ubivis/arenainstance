package com.ubivismedia.arenaplugin;

import org.bukkit.plugin.java.JavaPlugin;

public class ArenaInstance extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Arena Plugin wurde aktiviert!");
        // Initialisierung der Befehle und Event-Listener
        getCommand("arena").setExecutor(new ArenaCommand(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("Arena Plugin wurde deaktiviert!");
    }
}

