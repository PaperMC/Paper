package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an entity comes into contact with a portal
 * <p>
 * Cancelling this event prevents any further processing of the portal for that tick.
 * @see io.papermc.paper.event.entity.EntityInsideBlockEvent
 * @since 1.0.0 R1
 */
public class EntityPortalEnterEvent extends EntityEvent implements org.bukkit.event.Cancellable { // Paper
    private static final HandlerList handlers = new HandlerList();
    private final Location location;

    @Deprecated(since = "1.21") @io.papermc.paper.annotation.DoNotUse // Paper
    public EntityPortalEnterEvent(@NotNull final Entity entity, @NotNull final Location location) {
        // Paper start
        this(entity, location, org.bukkit.PortalType.CUSTOM);
    }
    @org.jetbrains.annotations.ApiStatus.Internal
    public EntityPortalEnterEvent(@NotNull final Entity entity, @NotNull final Location location, @NotNull final org.bukkit.PortalType portalType) {
        // Paper end
        super(entity);
        this.location = location;
        this.portalType = portalType; // Paper
    }

    /**
     * Gets the portal block the entity is touching
     *
     * @return The portal block the entity is touching
     */
    @NotNull
    public Location getLocation() {
        return location.clone(); // Paper - clone to avoid changes
    }

    // Paper start
    private boolean cancelled = false;
    private final org.bukkit.PortalType portalType;

    /**
     * Get the portal type.
     *
     * @return the portal type
     * @since 1.21
     */
    public org.bukkit.@NotNull PortalType getPortalType() {
        return this.portalType;
    }

    /**
     * @since 1.21
     */
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * @since 1.21
     */
    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }
    // Paper end

    /**
     * @since 1.1.0 R1
     */
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * @since 1.1.0 R1
     */
    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
