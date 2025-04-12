package org.bukkit.event.entity;

import org.bukkit.block.TrialSpawner;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an entity is spawned into a world by a trial spawner.
 * <p>
 * If this event is cancelled, the entity will not spawn.
 */
@ApiStatus.Experimental
public class TrialSpawnerSpawnEvent extends EntitySpawnEvent {

    private final TrialSpawner spawner;

    @ApiStatus.Internal
    public TrialSpawnerSpawnEvent(@NotNull final Entity spawnee, @NotNull final TrialSpawner spawner) {
        super(spawnee);
        this.spawner = spawner;
    }

    @NotNull
    public TrialSpawner getTrialSpawner() {
        return this.spawner;
    }
}
