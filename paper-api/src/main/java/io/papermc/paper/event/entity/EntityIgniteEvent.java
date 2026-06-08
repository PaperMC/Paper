package io.papermc.paper.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.checkerframework.checker.index.qual.Positive;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import static io.papermc.paper.util.BoundChecker.requirePositive;

/**
 * Called when an entity is ignited often by fire or redstone power.
 */
@NullMarked
public class EntityIgniteEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private int fuseTime;
    private boolean cancelled;

    @ApiStatus.Internal
    public EntityIgniteEvent(final Entity entity, final int fuseTime) {
        super(entity);
        this.fuseTime = fuseTime;
    }

    /**
     * Gets the amount of ticks required for this entity to explode.
     *
     * @return the amount of ticks required
     */
    public @Positive int getFuseTime() {
        return this.fuseTime;
    }

    /**
     * Sets the amount of ticks required for this entity to explode.
     *
     * @param ticks the amount of ticks required
     */
    public void setFuseTime(final @Positive int ticks) {
        this.fuseTime = requirePositive(ticks, "ticks");
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
