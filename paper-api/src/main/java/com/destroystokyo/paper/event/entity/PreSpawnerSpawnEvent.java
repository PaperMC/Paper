package com.destroystokyo.paper.event.entity;

import org.bukkit.Location;

/**
 * Called before an entity is spawned into a world by a spawner.
 * <p>
 * This only includes the spawner's location and not the full BlockState snapshot for performance reasons.
 * If you really need it you have to get the spawner yourself.
 */
public interface PreSpawnerSpawnEvent extends PreCreatureSpawnEvent { // todo javadocs?

    Location getSpawnerLocation();
}
