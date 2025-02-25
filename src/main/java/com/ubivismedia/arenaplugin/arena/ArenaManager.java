package com.ubivismedia.arenaplugin.arena;

import java.util.HashMap;
import java.util.Map;

public class ArenaManager {

    private final Map<String, Arena> arenas = new HashMap<>();

    public void createArena(String name) {
        if (arenas.containsKey(name)) {
            throw new IllegalArgumentException("Eine Arena mit diesem Namen existiert bereits!");
        }
        Arena arena = new Arena(name);
        arenas.put(name, arena);
    }

    public Arena getArena(String name) {
        return arenas.get(name);
    }

    public void removeArena(String name) {
        arenas.remove(name);
    }

    public boolean arenaExists(String name) {
        return arenas.containsKey(name);
    }

    public Map<String, Arena> getArenas() {
        return arenas;
    }
}
