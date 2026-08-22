package com.destroystokyo.paper.event.entity;

import org.bukkit.entity.Turtle;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEventNew;

/**
 * Fired when a Turtle decides to go home
 */
public interface TurtleGoHomeEvent extends EntityEventNew, Cancellable {

    /**
     * The turtle going home
     *
     * @return The turtle
     */
    @Override
    Turtle getEntity();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
