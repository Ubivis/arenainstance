package com.ubivismedia.arenaplugin.mobs;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;
import com.ubivismedia.arenaplugin.ArenaInstance;

public class MobBehaviorManager implements Listener {
    
    private final ArenaInstance plugin;
    
    public MobBehaviorManager(ArenaInstance plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onMobSpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        
        if (entity instanceof Animals || entity instanceof Villager) {
            transformToAggressive(entity);
        }
    }
    
    private void transformToAggressive(Entity entity) {
        if (entity instanceof Villager) {
            ((Villager) entity).setProfession(Villager.Profession.NITWIT);
            entity.setCustomName("Aggressiver Villager");
        }
        
        if (entity instanceof Animals) {
            entity.setCustomName("Wütendes Tier");
        }
        
        entity.setMetadata("aggressive", new FixedMetadataValue(plugin, true));
        entity.setCustomNameVisible(true);
    }
}
