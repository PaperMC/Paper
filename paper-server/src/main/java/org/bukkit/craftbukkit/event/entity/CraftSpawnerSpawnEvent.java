package org.bukkit.craftbukkit.event.entity;

import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.jspecify.annotations.Nullable;

public class CraftSpawnerSpawnEvent extends CraftEntitySpawnEvent implements SpawnerSpawnEvent {

    private final CreatureSpawner spawner;

    public CraftSpawnerSpawnEvent(final Entity spawnee, @Nullable final CreatureSpawner spawner) { // Paper
        super(spawnee);
        this.spawner = spawner;
    }

    @Override
    public @Nullable CreatureSpawner getSpawner() {
        return this.spawner;
    }
}
