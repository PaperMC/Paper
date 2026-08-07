package io.papermc.paper.event.entity;

import java.util.List;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Fired when two entities collide with each other.
 * If cancelled, the entities won't get pushed away from each other.
 * <p>
 * Note that even if cancelled, the client may still run its own collision unless
 * disabled via player teams.
 */
@NullMarked
public class EntityCollideWithEntityEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private boolean cancelled;
    private final List<Entity> entities;

    @ApiStatus.Internal
    public EntityCollideWithEntityEvent(final Entity entity1, final Entity entity2) {
        this.entities = List.of(entity1, entity2);
    }

    /**
     * Returns the entities involved in this event
     *
     * @return entities that are involved in this event
     */
    public List<Entity> getEntities() {
        return this.entities;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }
}
