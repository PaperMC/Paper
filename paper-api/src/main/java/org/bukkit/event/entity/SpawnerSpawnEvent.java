package org.bukkit.event.entity;

import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an entity is spawned into a world by a spawner.
 * <p>
 * If a Spawner Spawn event is cancelled, the entity will not spawn.
 */
public class SpawnerSpawnEvent extends EntitySpawnEvent {
    private final CreatureSpawner spawner;

    public SpawnerSpawnEvent(@NotNull final Entity spawnee, @org.jetbrains.annotations.Nullable final CreatureSpawner spawner) { // Paper
        super(spawnee);
        this.spawner = spawner;
    }

    /**
     * Gets the spawner tile state, or null
     * when the entity is spawned from a minecart
     * spawner.
     *
     * @return the spawner tile state
     */
    @org.jetbrains.annotations.Nullable // Paper
    public CreatureSpawner getSpawner() {
        return spawner;
    }
}
