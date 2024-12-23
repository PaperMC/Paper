package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a non-player entity is about to teleport because it is in
 * contact with a portal.
 * <p>
 * For players see {@link org.bukkit.event.player.PlayerPortalEvent}
 *
 * @since 1.4.7
 */
public class EntityPortalEvent extends EntityTeleportEvent {
    private static final HandlerList handlers = new HandlerList();
    private int searchRadius = 128;
    private boolean canCreatePortal = true;
    private int creationRadius = 16;
    private final org.bukkit.PortalType type; // Paper

    public EntityPortalEvent(@NotNull final Entity entity, @NotNull final Location from, @Nullable final Location to) {
        this(entity, from, to, 128); // Paper
    }

    public EntityPortalEvent(@NotNull Entity entity, @NotNull Location from, @Nullable Location to, int searchRadius) {
        super(entity, from, to);
        this.searchRadius = searchRadius;
        this.type = org.bukkit.PortalType.CUSTOM; // Paper
    }

    public EntityPortalEvent(@NotNull Entity entity, @NotNull Location from, @Nullable Location to, int searchRadius, boolean canCreatePortal, int creationRadius) {
        // Paper start
        this(entity, from, to, searchRadius, canCreatePortal, creationRadius, org.bukkit.PortalType.CUSTOM);
    }

    @ApiStatus.Internal
    public EntityPortalEvent(@NotNull Entity entity, @NotNull Location from, @Nullable Location to, int searchRadius, boolean canCreatePortal, int creationRadius, final @NotNull org.bukkit.PortalType portalType) {
        super(entity, from, to);
        this.type = portalType;
        // Paper end
        this.searchRadius = searchRadius;
        this.canCreatePortal = canCreatePortal;
        this.creationRadius = creationRadius;
    }

    // Paper start
    /**
     * Get the portal type relating to this event.
     *
     * @return the portal type
     * @since 1.19.3
     */
    public @NotNull org.bukkit.PortalType getPortalType() {
        return this.type;
    }
    /**
     * For {@link org.bukkit.PortalType#NETHER}, this is initially just the starting point
     * for the search for a portal to teleport to. It will initially just be the {@link #getFrom()}
     * scaled for dimension scaling and clamped to be inside the world border.
     * <p>
     * For {@link org.bukkit.PortalType#ENDER}, this will initially be the exact destination
     * either, the world spawn for <i>end->any world</i> or end spawn for <i>any world->end</i>.
     *
     * @return starting point for search or exact destination
     * @since 1.19.3
     */
    @Override
    public @Nullable Location getTo() {
        return super.getTo();
    }

    /**
     * See the description of {@link #getTo()}.
     * @param to starting point for search or exact destination
     *           or null to cancel
     * @since 1.19.3
     */
    @Override
    public void setTo(@Nullable final Location to) {
        super.setTo(to);
    }
    // Paper end

    /**
     * Set the Block radius to search in for available portals.
     *
     * @param searchRadius the radius in which to search for a portal from the
     * location
     * @since 1.15.1
     */
    public void setSearchRadius(int searchRadius) {
        this.searchRadius = searchRadius;
    }

    /**
     * Gets the search radius value for finding an available portal.
     *
     * @return the currently set search radius
     * @since 1.15.1
     */
    public int getSearchRadius() {
        return searchRadius;
    }

    /**
     * Returns whether the server will attempt to create a destination portal or
     * not.
     *
     * @return whether there should create be a destination portal created
     * @since 1.21
     */
    public boolean getCanCreatePortal() {
        return canCreatePortal;
    }

    /**
     * Sets whether the server should attempt to create a destination portal or
     * not.
     *
     * @param canCreatePortal Sets whether there should be a destination portal
     * created
     * @since 1.21
     */
    public void setCanCreatePortal(boolean canCreatePortal) {
        this.canCreatePortal = canCreatePortal;
    }

    /**
     * Sets the maximum radius the world is searched for a free space from the
     * given location.
     *
     * If enough free space is found then the portal will be created there, if
     * not it will force create with air-space at the target location.
     *
     * Does not apply to end portal target platforms which will always appear at
     * the target location.
     *
     * @param creationRadius the radius in which to create a portal from the
     * location
     * @since 1.21
     */
    public void setCreationRadius(int creationRadius) {
        this.creationRadius = creationRadius;
    }

    /**
     * Gets the maximum radius the world is searched for a free space from the
     * given location.
     *
     * If enough free space is found then the portal will be created there, if
     * not it will force create with air-space at the target location.
     *
     * Does not apply to end portal target platforms which will always appear at
     * the target location.
     *
     * @return the currently set creation radius
     * @since 1.21
     */
    public int getCreationRadius() {
        return creationRadius;
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
