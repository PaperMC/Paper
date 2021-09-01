package org.bukkit.event.world;

import java.util.List;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when entities are unloaded.
 *
 * The provided chunk may or may not be loaded.
 */
public class EntitiesUnloadEvent extends ChunkEvent {

    private static final HandlerList handlers = new HandlerList();
    private final List<Entity> entities;

    public EntitiesUnloadEvent(@NotNull Chunk chunk, @NotNull List<Entity> entities) {
        super(chunk);
        this.entities = entities;
    }

    /**
     * Get the entities which are being unloaded.
     *
     * @return unmodifiable list of unloaded entities.
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
