package org.bukkit.craftbukkit.event.world;

import java.util.List;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.jetbrains.annotations.Unmodifiable;

public class CraftEntitiesLoadEvent extends CraftChunkEvent implements EntitiesLoadEvent {

    private final List<Entity> entities;

    public CraftEntitiesLoadEvent(final Chunk chunk, final List<Entity> entities) {
        super(chunk);
        this.entities = entities;
    }

    @Override
    public @Unmodifiable List<Entity> getEntities() {
        return this.entities;
    }

    @Override
    public HandlerList getHandlers() {
        return EntitiesLoadEvent.getHandlerList();
    }
}
