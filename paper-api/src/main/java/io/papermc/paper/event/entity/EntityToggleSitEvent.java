package io.papermc.paper.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Is called when an entity sits down or stands up.
 */
@NullMarked
public class EntityToggleSitEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final boolean isSitting;
    private boolean cancelled;

    @ApiStatus.Internal
    public EntityToggleSitEvent(final Entity entity, final boolean isSitting) {
        super(entity);
        this.isSitting = isSitting;
    }

    /**
     * Gets the new sitting state that the entity will change to.
     *
     * @return If it's going to sit or not.
     */
    public boolean getSittingState() {
        return this.isSitting;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
