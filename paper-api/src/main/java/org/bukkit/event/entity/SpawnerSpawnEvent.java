package org.bukkit.event.entity;

import org.bukkit.block.CreatureSpawner;
import org.jspecify.annotations.Nullable;

/**
 * Called when an entity is spawned into a world by a spawner.
 * <p>
 * If this event is cancelled, the entity will not spawn.
 */
public interface SpawnerSpawnEvent extends EntitySpawnEvent {

    /**
     * Gets the spawner tile state, or null
     * when the entity is spawned from a minecart
     * spawner.
     *
     * @return the spawner tile state
     */
    @Nullable CreatureSpawner getSpawner();
}
