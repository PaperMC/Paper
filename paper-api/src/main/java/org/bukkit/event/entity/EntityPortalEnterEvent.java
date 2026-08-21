package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.PortalType;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when an entity comes into contact with a portal
 * <p>
 * Cancelling this event prevents any further processing of the portal for that tick.
 *
 * @see io.papermc.paper.event.entity.EntityInsideBlockEvent
 */
public interface EntityPortalEnterEvent extends EntityEventNew, Cancellable {

    /**
     * Gets the portal block the entity is touching
     *
     * @return The portal block the entity is touching
     */
    Location getLocation();

    /**
     * Get the portal type.
     *
     * @return the portal type
     */
    PortalType getPortalType();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
