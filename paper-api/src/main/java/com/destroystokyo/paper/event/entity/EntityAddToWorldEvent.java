package com.destroystokyo.paper.event.entity;

import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.world.WorldEvent;

/**
 * Fired any time an entity is being added to the world for any reason (including a chunk loading).
 * <p>
 * Not to be confused with {@link CreatureSpawnEvent}
 */
public interface EntityAddToWorldEvent extends EntityEvent, WorldEvent {

    /**
     * @return The world that the entity is being added to
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
