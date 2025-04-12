package org.bukkit.event.entity;

import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when an entity is spawned into a world by a spawner.
 * <p>
 * If this event is cancelled, the entity will not spawn.
 */
public class SpawnerSpawnEvent extends EntitySpawnEvent {

    private final CreatureSpawner spawner;

    @ApiStatus.Internal
    public SpawnerSpawnEvent(@NotNull final Entity spawnee, @Nullable final CreatureSpawner spawner) { // Paper
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
    @Nullable
    public CreatureSpawner getSpawner() {
        return this.spawner;
    }
}
