package com.destroystokyo.paper.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called before an entity is spawned into a world by a spawner.
 * <p>
 * This only includes the spawner's location and not the full BlockState snapshot for performance reasons.
 * If you really need it you have to get the spawner yourself.
 */
@NullMarked
public class PreSpawnerSpawnEvent extends PreCreatureSpawnEvent {

    private final Location spawnerLocation;

    @ApiStatus.Internal
    public PreSpawnerSpawnEvent(final Location location, final EntityType type, final Location spawnerLocation) {
        super(location, type, CreatureSpawnEvent.SpawnReason.SPAWNER);
        this.spawnerLocation = spawnerLocation;
    }

    public Location getSpawnerLocation() {
        return this.spawnerLocation.clone();
    }
}
