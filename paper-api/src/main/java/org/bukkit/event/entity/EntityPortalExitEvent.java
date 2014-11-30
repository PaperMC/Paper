package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;

/**
 * Called before an entity exits a portal.
 * <p>
 * This event allows you to modify the velocity of the entity after they have
 * successfully exited the portal.
 */
public class EntityPortalExitEvent extends EntityTeleportEvent {
    private static final HandlerList handlers = new HandlerList();
    private Vector before;
    private Vector after;

    public EntityPortalExitEvent(final Entity entity, final Location from, final Location to, final Vector before, final Vector after) {
        super(entity, from, to);
        this.before = before;
        this.after = after;
    }

    /**
     * Gets a copy of the velocity that the entity has before entering the
     * portal.
     *
     * @return velocity of entity before entering the portal
     */
    public Vector getBefore() {
        return this.before.clone();
    }

    /**
     * Gets a copy of the velocity that the entity will have after exiting the
     * portal.
     *
     * @return velocity of entity after exiting the portal
     */
    public Vector getAfter() {
        return this.after.clone();
    }

    /**
     * Sets the velocity that the entity will have after exiting the portal.
     * 
     * @param after the velocity after exiting the portal
     */
    public void setAfter(Vector after) {
        this.after = after.clone();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}