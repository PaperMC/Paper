package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

/**
 * Called before an entity exits a portal.
 * <p>
 * This event allows you to modify the velocity of the entity after they have
 * successfully exited the portal.
 * <p>
 * Cancelling this event does not prevent the teleport, but it does prevent
 * any changes to velocity and location from taking place.
 */
public class EntityPortalExitEvent extends EntityTeleportEvent {
    private static final HandlerList handlers = new HandlerList();
    private Vector before;
    private Vector after;

    public EntityPortalExitEvent(@NotNull final Entity entity, @NotNull final Location from, @NotNull final Location to, @NotNull final Vector before, @NotNull final Vector after) {
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
    @NotNull
    public Vector getBefore() {
        return this.before.clone();
    }

    /**
     * Gets a copy of the velocity that the entity will have after exiting the
     * portal.
     *
     * @return velocity of entity after exiting the portal
     */
    @NotNull
    public Vector getAfter() {
        return this.after.clone();
    }

    /**
     * Sets the velocity that the entity will have after exiting the portal.
     *
     * @param after the velocity after exiting the portal
     */
    public void setAfter(@NotNull Vector after) {
        this.after = after.clone();
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
