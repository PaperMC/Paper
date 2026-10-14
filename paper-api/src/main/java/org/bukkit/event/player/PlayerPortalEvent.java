package org.bukkit.event.player;

import io.papermc.paper.entity.TeleportFlag;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Set;

/**
 * Called when a player is about to teleport because it is in contact with a
 * portal which will generate an exit portal.
 * <p>
 * For other entities see {@link org.bukkit.event.entity.EntityPortalEvent}
 */
public class PlayerPortalEvent extends PlayerTeleportEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private int searchRadius = 128;
    private boolean canCreatePortal = true;
    private int creationRadius = 16;

    @ApiStatus.Internal
    public PlayerPortalEvent(@NotNull final Player player, @NotNull final Location from, @Nullable final Location to) {
        super(player, from, to);
    }

    @ApiStatus.Internal
    public PlayerPortalEvent(@NotNull Player player, @NotNull Location from, @Nullable Location to, @NotNull TeleportCause cause) {
        super(player, from, to, cause);
    }

    @ApiStatus.Internal
    public PlayerPortalEvent(@NotNull Player player, @NotNull Location from, @Nullable Location to, @NotNull TeleportCause cause, int searchRadius, boolean canCreatePortal, int creationRadius) {
        super(player, from, to, cause);
        this.searchRadius = searchRadius;
        this.canCreatePortal = canCreatePortal;
        this.creationRadius = creationRadius;
    }

    /**
     * For {@link TeleportCause#NETHER_PORTAL}, this is initially just the starting point
     * for the search for a portal to teleport to. It will initially just be the {@link #getFrom()}
     * scaled for dimension scaling and clamped to be inside the world border.
     * <p>
     * For {@link TeleportCause#END_PORTAL}, this will initially be the exact destination
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
     *
     * @param to starting point for search or exact destination
     */
    @Override
    public void setTo(@NotNull final Location to) {
        super.setTo(to);
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
     * No effect
     *
     * @return no effect
     * @deprecated No effect
     */
    @Deprecated(forRemoval = true)
    @Override
    public boolean willDismountPlayer() {
        return super.willDismountPlayer();
    }

    /**
     * No effect
     *
     * @return no effect
     * @deprecated No effect
     */
    @Deprecated(forRemoval = true)
    @Override
    public @NotNull Set<TeleportFlag.Relative> getRelativeTeleportationFlags() {
        return super.getRelativeTeleportationFlags();
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
