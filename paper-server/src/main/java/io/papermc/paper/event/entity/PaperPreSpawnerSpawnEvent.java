package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.PreSpawnerSpawnEvent;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class PaperPreSpawnerSpawnEvent extends PaperPreCreatureSpawnEvent implements PreSpawnerSpawnEvent {

    private final Location spawnerLocation;

    public PaperPreSpawnerSpawnEvent(final Location location, final EntityType type, final Location spawnerLocation) {
        super(location, type, CreatureSpawnEvent.SpawnReason.SPAWNER);
        this.spawnerLocation = spawnerLocation;
    }

    @Override
    public Location getSpawnerLocation() {
        return this.spawnerLocation.clone();
    }
}
