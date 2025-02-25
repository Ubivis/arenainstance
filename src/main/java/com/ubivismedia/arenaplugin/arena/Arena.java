package com.ubivismedia.arenaplugin.arena;

import org.bukkit.block.Block;
import java.util.HashMap;
import java.util.Map;

public class Arena {
    private final String name;
    private final Map<Block, Integer> waveAmount = new HashMap<>();
    private final Map<Block, Integer> waveInterval = new HashMap<>();
    private final Map<Block, Integer> waveNumber = new HashMap<>();
    private final Map<Block, String> mobType = new HashMap<>();

    public Arena(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public int getWaveAmount(Block block) {
        return waveAmount.getOrDefault(block, 5);
    }
    
    public void setWaveAmount(Block block, int amount) {
        waveAmount.put(block, amount);
    }
    
    public int getWaveInterval(Block block) {
        return waveInterval.getOrDefault(block, 10);
    }
    
    public void setWaveInterval(Block block, int interval) {
        waveInterval.put(block, interval);
    }
    
    public int getWaveNumber(Block block) {
        return waveNumber.getOrDefault(block, 1);
    }
    
    public void setWaveNumber(Block block, int number) {
        waveNumber.put(block, number);
    }
    
    public String getMobType(Block block) {
        return mobType.getOrDefault(block, "ZOMBIE");
    }
    
    public void setMobType(Block block, String type) {
        mobType.put(block, type);
    }
    
    public String getNextMobType(Block block) {
        String[] mobs = {"ZOMBIE", "SKELETON", "SPIDER", "CREEPER"};
        String current = getMobType(block);
        for (int i = 0; i < mobs.length; i++) {
            if (mobs[i].equals(current)) {
                return mobs[(i + 1) % mobs.length];
            }
        }
        return mobs[0];
    }
}
