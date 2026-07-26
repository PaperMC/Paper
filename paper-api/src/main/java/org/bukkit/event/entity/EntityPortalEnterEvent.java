package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.PortalType;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an entity comes into contact with a portal
 * <p>
 * Cancelling this event prevents any further processing of the portal for that tick.
 * @see io.papermc.paper.event.entity.EntityInsideBlockEvent
 */
public class EntityPortalEnterEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Location location;
    private final PortalType portalType;

    private boolean cancelled;

    @ApiStatus.Internal
    @Deprecated(since = "1.21")
    public EntityPortalEnterEvent(@NotNull final Entity entity, @NotNull final Location location) {
        this(entity, location, PortalType.CUSTOM);
    }

    @ApiStatus.Internal
    public EntityPortalEnterEvent(@NotNull final Entity entity, @NotNull final Location location, @NotNull final PortalType portalType) {
        super(entity);
        this.location = location;
        this.portalType = portalType;
    }

    /**
     * Gets the portal block the entity is touching
     *
     * @return The portal block the entity is touching
     */
    @NotNull
    public Location getLocation() {
        return location.clone();
    }

    /**
     * Get the portal type.
     *
     * @return the portal type
     */
    public org.bukkit.@NotNull PortalType getPortalType() {
        return this.portalType;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
