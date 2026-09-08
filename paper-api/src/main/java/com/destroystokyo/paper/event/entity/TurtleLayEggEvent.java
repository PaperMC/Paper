package com.destroystokyo.paper.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Turtle;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.checkerframework.common.value.qual.IntRange;

/**
 * Fired when a Turtle lays eggs
 */
public interface TurtleLayEggEvent extends EntityEvent, Cancellable {

    /**
     * The turtle laying the eggs
     *
     * @return The turtle
     */
    @Override
    Turtle getEntity();

    /**
     * Get the location where the eggs are being laid
     *
     * @return Location of eggs
     */
    Location getLocation();

    /**
     * Get the number of eggs being laid
     *
     * @return Number of eggs
     */
    @IntRange(from = 1, to = 4) int getEggCount();

    /**
     * Set the number of eggs being laid
     *
     * @param eggCount Number of eggs
     */
    void setEggCount(@IntRange(from = 0, to = 4) int eggCount);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
