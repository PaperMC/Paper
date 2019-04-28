package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an entity combusts.
 * <p>
 * If an Entity Combust event is cancelled, the entity will not combust.
 */
public class EntityCombustEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private int duration;
    private boolean cancel;

    public EntityCombustEvent(@NotNull final Entity combustee, final int duration) {
        super(combustee);
        this.duration = duration;
        this.cancel = false;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * @return the amount of time (in seconds) the combustee should be alight
     *     for
     */
    public int getDuration() {
        return duration;
    }

    /**
     * The number of seconds the combustee should be alight for.
     * <p>
     * This value will only ever increase the combustion time, not decrease
     * existing combustion times.
     *
     * @param duration the time in seconds to be alight for.
     */
    public void setDuration(int duration) {
        this.duration = duration;
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
