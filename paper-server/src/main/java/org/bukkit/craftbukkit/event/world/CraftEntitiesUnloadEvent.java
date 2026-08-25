package org.bukkit.craftbukkit.event.world;

import java.util.List;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.EntitiesUnloadEvent;
import org.jetbrains.annotations.Unmodifiable;

public class CraftEntitiesUnloadEvent extends CraftChunkEvent implements EntitiesUnloadEvent {

    private final List<Entity> entities;

    public CraftEntitiesUnloadEvent(final Chunk chunk, final List<Entity> entities) {
        super(chunk);
        this.entities = entities;
    }

    @Override
    public @Unmodifiable List<Entity> getEntities() {
        return this.entities;
    }

    @Override
    public HandlerList getHandlers() {
        return EntitiesUnloadEvent.getHandlerList();
    }
}
