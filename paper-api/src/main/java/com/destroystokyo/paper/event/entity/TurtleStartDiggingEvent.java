package com.destroystokyo.paper.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Turtle;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEventNew;

/**
 * Fired when a Turtle starts digging to lay eggs
 */
public interface TurtleStartDiggingEvent extends EntityEventNew, Cancellable {

    /**
     * The turtle digging
     *
     * @return The turtle
     */
    @Override
    Turtle getEntity();

    /**
     * Get the location where the turtle is digging
     *
     * @return Location where digging
     */
    Location getLocation();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
