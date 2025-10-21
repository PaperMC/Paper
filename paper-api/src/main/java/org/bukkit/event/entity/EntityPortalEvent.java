package org.bukkit.event.entity;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.PortalType;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a non-player entity is about to teleport because it is in
 * contact with a portal.
 * <p>
 * For players see {@link org.bukkit.event.player.PlayerPortalEvent}
 */
public class EntityPortalEvent extends EntityTeleportEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final PortalType type;
    private int searchRadius;
    private boolean canCreatePortal;
    private int creationRadius;
    private WorldBorder worldBorder = null;

    @ApiStatus.Internal
    public EntityPortalEvent(@NotNull final Entity entity, @NotNull final Location from, @NotNull final Location to) {
        this(entity, from, to, 128);
    }

    @ApiStatus.Internal
    public EntityPortalEvent(@NotNull Entity entity, @NotNull Location from, @NotNull Location to, int searchRadius) {
        this(entity, from, to, searchRadius, true, 16);
    }

    @ApiStatus.Internal
    public EntityPortalEvent(@NotNull Entity entity, @NotNull Location from, @NotNull Location to, int searchRadius, boolean canCreatePortal, int creationRadius) {
        this(entity, from, to, searchRadius, canCreatePortal, creationRadius, PortalType.CUSTOM);
    }

    @ApiStatus.Internal
    public EntityPortalEvent(@NotNull Entity entity, @NotNull Location from, @NotNull Location to, int searchRadius, boolean canCreatePortal, int creationRadius, final @NotNull PortalType portalType) {
        super(entity, from, to);
        this.type = portalType;
        this.searchRadius = searchRadius;
        this.canCreatePortal = canCreatePortal;
        this.creationRadius = creationRadius;
    }

    /**
     * For {@link PortalType#NETHER}, this is initially just the starting point
     * for the search for a portal to teleport to. It will initially just be the {@link #getFrom()}
     * scaled for dimension scaling and clamped to be inside the world border.
     * <p>
     * For {@link PortalType#ENDER}, this will initially be the exact destination
     * either, the world spawn for <i>end->any world</i> or end spawn for <i>any world->end</i>.
     *
     * @return starting point for search or exact destination
     */
    @Override
    public @NotNull Location getTo() {
        return super.getTo();
    }

    /**
     * See the description of {@link #getTo()}.
     * @param to starting point for search or exact destination
     *           or {@code null} to cancel
     */
    @Override
    public void setTo(@NotNull final Location to) {
        super.setTo(to);
    }

    /**
     * Get the portal type relating to this event.
     *
     * @return the portal type
     */
    public @NotNull PortalType getPortalType() {
        return this.type;
    }

    /**
     * Set the Block radius to search in for available portals.
     *
     * @param searchRadius the radius in which to search for a portal from the
     * location
     */
    public void setSearchRadius(int searchRadius) {
        this.searchRadius = searchRadius;
    }

    /**
     * Gets the search radius value for finding an available portal.
     *
     * @return the currently set search radius
     */
    public int getSearchRadius() {
        return this.searchRadius;
    }

    /**
     * Returns whether the server will attempt to create a destination portal or
     * not.
     *
     * @return whether there should create be a destination portal created
     */
    public boolean getCanCreatePortal() {
        return this.canCreatePortal;
    }

    /**
     * Sets whether the server should attempt to create a destination portal or
     * not.
     *
     * @param canCreatePortal Sets whether there should be a destination portal
     * created
     */
    public void setCanCreatePortal(boolean canCreatePortal) {
        this.canCreatePortal = canCreatePortal;
    }

    /**
     * Sets the maximum radius the world is searched for a free space from the
     * given location.
     * <p>
     * If enough free space is found then the portal will be created there, if
     * not it will force create with air-space at the target location.
     * <p>
     * Does not apply to end portal target platforms which will always appear at
     * the target location.
     *
     * @param creationRadius the radius in which to create a portal from the
     * location
     */
    public void setCreationRadius(int creationRadius) {
        this.creationRadius = creationRadius;
    }

    /**
     * Gets the maximum radius the world is searched for a free space from the
     * given location.
     * <p>
     * If enough free space is found then the portal will be created there, if
     * not it will force create with air-space at the target location.
     * <p>
     * Does not apply to end portal target platforms which will always appear at
     * the target location.
     *
     * @return the currently set creation radius
     */
    public int getCreationRadius() {
        return this.creationRadius;
    }

    /**
     * Sets the {@link WorldBorder} that portal search and optionally creation will be limited to.
     * <p>
     * Does not apply to end portal target platforms which will always appear at
     * the target location.
     *
     * @param worldBorder the {@link WorldBorder} that portal search and optionally creation will be limited to.
     */
    public void setWorldBorder(@NotNull WorldBorder worldBorder) {
        this.worldBorder = worldBorder;
    }

    /**
     * Sets the {@link WorldBorder} that portal search and optionally creation will be limited to.
     * <p>
     * Does not apply to end portal target platforms which will always appear at
     * the target location.
     *
     * @param center the center of the world border.
     * @param size the side length of the world border.
     */
    public void setWorldBorder(@NotNull Location center, double size) {
        this.worldBorder = Bukkit.createWorldBorder();
        this.worldBorder.setCenter(center);
        this.worldBorder.setSize(size);
    }

    /**
     * Gets the {@link WorldBorder} that portal search and optionally creation will be limited to.
     * <p>
     * Does not apply to end portal target platforms which will always appear at
     * the target location.
     *
     * @return the {@link WorldBorder} that portal search and optionally creation will be limited to.
     */
    public @NotNull WorldBorder getWorldBorder() {
        if  (this.worldBorder == null) {
            var worldWorldBorder = this.getTo().getWorld().getWorldBorder();
            this.worldBorder = Bukkit.createWorldBorder();
            this.worldBorder.setCenter(worldWorldBorder.getCenter().clone());
            this.worldBorder.setSize(worldWorldBorder.getSize());
        }

        return this.worldBorder;
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
