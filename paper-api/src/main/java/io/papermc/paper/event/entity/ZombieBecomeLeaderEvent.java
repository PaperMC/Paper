package io.papermc.paper.event.entity;

import org.bukkit.entity.Zombie;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a zombie becomes a leader zombie and gets extra attributes
 * and the ability to break door.
 * <p>
 * If the event is cancelled, the zombie will not have the extra attributes
 * or door-breaking ability.
 */

@NullMarked
public class ZombieBecomeLeaderEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private boolean cancelled;

    @ApiStatus.Internal
    public ZombieBecomeLeaderEvent(final Zombie zombie) {
        super(zombie);
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
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
