package org.bukkit.event.entity;

import org.bukkit.block.TrialSpawner;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an entity is spawned into a world by a trial spawner.
 * <p>
 * If a Trial Spawner Spawn event is cancelled, the entity will not spawn.
 *
 * @since 1.21
 */
@ApiStatus.Experimental
public class TrialSpawnerSpawnEvent extends EntitySpawnEvent {
    private final TrialSpawner spawner;

    public TrialSpawnerSpawnEvent(@NotNull final Entity spawnee, @NotNull final TrialSpawner spawner) {
        super(spawnee);
        this.spawner = spawner;
    }

    @NotNull
    public TrialSpawner getTrialSpawner() {
        return spawner;
    }
}
