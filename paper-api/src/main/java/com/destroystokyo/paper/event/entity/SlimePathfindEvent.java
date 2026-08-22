package com.destroystokyo.paper.event.entity;

import org.bukkit.entity.AbstractCubeMob;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

/**
 * Fired when a Slime decides to start pathfinding.
 * <p>
 * This event does not fire for the entity's actual movement. Only when it
 * is choosing to start moving.
 */
public interface SlimePathfindEvent extends EntityEvent, Cancellable {

    /**
     * The Slime that is pathfinding.
     *
     * @return The Slime that is pathfinding.
     */
    @Override
    AbstractCubeMob getEntity();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
