package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class CraftCreatureSpawnEvent extends CraftEntitySpawnEvent implements CreatureSpawnEvent {

    private final SpawnReason spawnReason;

    public CraftCreatureSpawnEvent(final net.minecraft.world.entity.LivingEntity entity, final SpawnReason spawnReason) {
        super(entity);
        this.spawnReason = spawnReason;
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }

    @Override
    public SpawnReason getSpawnReason() {
        return this.spawnReason;
    }
}
