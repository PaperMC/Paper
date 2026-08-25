package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.PhantomPreSpawnEvent;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class PaperPhantomPreSpawnEvent extends PaperPreCreatureSpawnEvent implements PhantomPreSpawnEvent {

    private final Entity entity;

    public PaperPhantomPreSpawnEvent(final Location location, final Entity entity, final CreatureSpawnEvent.SpawnReason reason) {
        super(location, EntityType.PHANTOM, reason);
        this.entity = entity;
    }

    @Override
    public Entity getSpawningEntity() {
        return this.entity;
    }
}
