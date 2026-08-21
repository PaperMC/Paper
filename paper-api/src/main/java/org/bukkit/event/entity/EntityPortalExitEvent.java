package org.bukkit.event.entity;

import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;

/**
 * Called before an entity exits a portal.
 * <p>
 * This event allows you to modify the velocity of the entity after they have
 * successfully exited the portal.
 * <p>
 * Cancelling this event does not prevent the teleport, but it does prevent
 * any changes to velocity and location from taking place.
 */
public interface EntityPortalExitEvent extends EntityTeleportEvent {

    /**
     * Gets a copy of the velocity that the entity has before entering the
     * portal.
     *
     * @return velocity of entity before entering the portal
     */
    Vector getBefore();

    /**
     * Gets a copy of the velocity that the entity will have after exiting the
     * portal.
     *
     * @return velocity of entity after exiting the portal
     */
    Vector getAfter();

    /**
     * Sets the velocity that the entity will have after exiting the portal.
     *
     * @param after the velocity after exiting the portal
     */
    void setAfter(Vector after);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
