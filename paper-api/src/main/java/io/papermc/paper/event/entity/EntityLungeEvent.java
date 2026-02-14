package io.papermc.paper.event.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a living entity tries to lunge with a spear
 */
@NullMarked
public class EntityLungeEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private boolean cancelled;

    @ApiStatus.Internal
    public EntityLungeEvent(final LivingEntity entity) {
        super(entity);
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Set whether to cancel the lunge. If canceled, the living entity will not lunge forward
     * @param cancel {@code true} if you wish to cancel this event
     */
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
