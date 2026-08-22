package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when an entity is spawned into a world.
 * <p>
 * If this event is cancelled, the entity will not spawn.
 */
public interface EntitySpawnEvent extends EntityEvent, Cancellable {

    /**
     * Gets the location at which the entity is spawning.
     *
     * @return The location at which the entity is spawning
     */
    default Location getLocation() {
        return this.getEntity().getLocation();
    }

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
