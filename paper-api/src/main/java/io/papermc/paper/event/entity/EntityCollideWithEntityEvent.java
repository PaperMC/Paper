package io.papermc.paper.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import java.util.List;

/**
 * Fired when two entities collide with each other.
 * If cancelled, the entities won't get pushed away from each other.
 */
public class EntityCollideWithEntityEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private boolean cancelled;
    private final List<Entity> entities;

    @ApiStatus.Internal
    public EntityCollideWithEntityEvent(@NotNull Entity entity1, @NotNull Entity entity2) {
        entities = List.of(entity1, entity2);
    }

    /**
     * Returns the entities involved in this event
     *
     * @return entities that are involved in this event
     */
    public @NotNull List<Entity> getEntities() {
        return entities;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }
}
