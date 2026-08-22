package com.destroystokyo.paper.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Turtle;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEventNew;

/**
 * Fired when a Turtle lays eggs
 */
public interface TurtleLayEggEvent extends EntityEventNew, Cancellable {

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
    int getEggCount();

    /**
     * Set the number of eggs being laid
     *
     * @param eggCount Number of eggs
     */
    void setEggCount(int eggCount);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
