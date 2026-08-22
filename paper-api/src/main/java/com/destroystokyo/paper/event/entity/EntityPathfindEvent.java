package com.destroystokyo.paper.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEventNew;
import org.jspecify.annotations.Nullable;

/**
 * Fired when an Entity decides to start moving towards a location.
 * <p>
 * This event does not fire for the entities actual movement. Only when it
 * is choosing to start moving to a location.
 */
public interface EntityPathfindEvent extends EntityEventNew, Cancellable {

    /**
     * The Entity that is pathfinding.
     *
     * @return The Entity that is pathfinding.
     */
    @Override
    Entity getEntity();

    /**
     * If the Entity is trying to pathfind to an entity, this is the entity in relation.
     * <br>
     * Otherwise, this will return {@code null}.
     *
     * @return The entity target or {@code null}
     */
    @Nullable Entity getTargetEntity();

    /**
     * The Location of where the entity is about to move to.
     * <br>
     * Note that if the target happened to of been an entity
     *
     * @return Location of where the entity is trying to pathfind to.
     */
    Location getLoc(); // todo rename

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
