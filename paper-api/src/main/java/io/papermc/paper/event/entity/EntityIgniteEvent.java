package io.papermc.paper.event.entity;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEventNew;
import org.checkerframework.checker.index.qual.Positive;

/**
 * Called when an entity is ignited often by fire or redstone power.
 */
public interface EntityIgniteEvent extends EntityEventNew, Cancellable {

    /**
     * Gets the amount of ticks required for this entity to explode.
     *
     * @return the amount of ticks required
     */
    @Positive int getFuseTime();

    /**
     * Sets the amount of ticks required for this entity to explode.
     *
     * @param ticks the amount of ticks required
     */
    void setFuseTime(@Positive int ticks);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
