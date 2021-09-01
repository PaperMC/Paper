package org.bukkit.event.world;

import java.util.List;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when entities are loaded.
 *
 * The provided chunk may or may not be loaded.
 */
public class EntitiesLoadEvent extends ChunkEvent {

    private static final HandlerList handlers = new HandlerList();
    private final List<Entity> entities;

    public EntitiesLoadEvent(@NotNull Chunk chunk, @NotNull List<Entity> entities) {
        super(chunk);
        this.entities = entities;
    }

    /**
     * Get the entities which are being loaded.
     *
     * @return unmodifiable list of loaded entities.
     */
    @NotNull
    public List<Entity> getEntities() {
        return entities;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
