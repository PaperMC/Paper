package org.bukkit.craftbukkit.event.entity;

import org.bukkit.block.TrialSpawner;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.TrialSpawnerSpawnEvent;

public class CraftTrialSpawnerSpawnEvent extends CraftEntitySpawnEvent implements TrialSpawnerSpawnEvent {

    private final TrialSpawner spawner;

    public CraftTrialSpawnerSpawnEvent(final Entity spawnee, final TrialSpawner spawner) {
        super(spawnee);
        this.spawner = spawner;
    }

    @Override
    public TrialSpawner getTrialSpawner() {
        return this.spawner;
    }
}
