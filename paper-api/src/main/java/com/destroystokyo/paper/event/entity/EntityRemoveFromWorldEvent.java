package com.destroystokyo.paper.event.entity;

import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.world.WorldEvent;

/**
 * Fired any time an entity is being removed from a world for any reason (including a chunk unloading).
 * Note: The entity is updated prior to this event being called, as such, the entity's world may not be equal to {@link #getWorld()}.
 */
public interface EntityRemoveFromWorldEvent extends EntityEvent, WorldEvent {

    /**
     * @return The world that the entity is being removed from
     */
    @Override
    World getWorld();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
