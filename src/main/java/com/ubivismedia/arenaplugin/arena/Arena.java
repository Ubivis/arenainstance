package com.ubivismedia.arenaplugin.arena;

import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Arena {
    private final String name;
    private final Map<Block, Integer> waveAmount = new HashMap<>();
    private final Map<Block, Integer> waveInterval = new HashMap<>();
    private final Map<Block, Integer> waveNumber = new HashMap<>();
    private final Map<Block, String> mobType = new HashMap<>();
    private final boolean mythicMobsInstalled;
    private final List<String> availableMobs;
    
    public Arena(String name) {
        this.name = name;
        this.mythicMobsInstalled = Bukkit.getPluginManager().isPluginEnabled("MythicMobs");
        this.availableMobs = loadAvailableMobs();
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
        int index = availableMobs.indexOf(getMobType(block));
        return availableMobs.get((index + 1) % availableMobs.size());
    }
    
    private List<String> loadAvailableMobs() {
        List<String> mobs = new ArrayList<>();
        mobs.add("ZOMBIE");
        mobs.add("SKELETON");
        mobs.add("SPIDER");
        mobs.add("CREEPER");
        
        if (mythicMobsInstalled) {
            BukkitAPIHelper mythicHelper = new BukkitAPIHelper();
            mobs.addAll(mythicHelper.getAllMythicMobNames());
        }
        
        return mobs;
    }
}
