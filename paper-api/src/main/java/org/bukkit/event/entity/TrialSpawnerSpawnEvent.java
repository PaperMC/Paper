package org.bukkit.event.entity;

import org.bukkit.block.TrialSpawner;

/**
 * Called when an entity is spawned into a world by a trial spawner.
 * <p>
 * If this event is cancelled, the entity will not spawn.
 */
public interface TrialSpawnerSpawnEvent extends EntitySpawnEvent {

    TrialSpawner getTrialSpawner();
}
