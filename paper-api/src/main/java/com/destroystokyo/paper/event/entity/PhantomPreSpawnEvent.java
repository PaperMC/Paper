package com.destroystokyo.paper.event.entity;

import org.bukkit.entity.Entity;

/**
 * Called when a phantom is spawned for an exhausted player
 */
public interface PhantomPreSpawnEvent extends PreCreatureSpawnEvent {

    /**
     * Get the entity this phantom is spawning for
     *
     * @return the Entity
     */
    Entity getSpawningEntity();
}
