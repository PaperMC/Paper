package com.destroystokyo.paper.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a phantom is spawned for an exhausted player
 */
@NullMarked
public class PhantomPreSpawnEvent extends PreCreatureSpawnEvent {

    private final Entity entity;

    @ApiStatus.Internal
    public PhantomPreSpawnEvent(final Location location, final Entity entity, final CreatureSpawnEvent.SpawnReason reason) {
        super(location, EntityType.PHANTOM, reason);
        this.entity = entity;
    }

    /**
     * Get the entity this phantom is spawning for
     *
     * @return the Entity
     */
    public Entity getSpawningEntity() {
        return this.entity;
    }
}
