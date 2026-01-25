package org.bukkit.event.player;

import io.papermc.paper.entity.TeleportFlag;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;

/**
 * Called when a player is about to teleport because it is in contact with a
 * portal which will generate an exit portal.
 * <p>
 * For other entities see {@link org.bukkit.event.entity.EntityPortalEvent}
 */
public interface PlayerPortalEvent extends PlayerTeleportEvent {

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
    Location getTo();

    /**
     * See the description of {@link #getTo()}.
     *
     * @param to starting point for search or exact destination
     */
    @Override
    void setTo(Location to);

    /**
     * Gets the search radius value for finding an available portal.
     *
     * @return the currently set search radius
     */
    int getSearchRadius();

    /**
     * Set the Block radius to search in for available portals.
     *
     * @param searchRadius the radius in which to search for a portal from the
     * location
     */
    void setSearchRadius(int searchRadius);

    /**
     * Returns whether the server will attempt to create a destination portal or
     * not.
     *
     * @return whether there should create be a destination portal created
     */
    boolean getCanCreatePortal();

    /**
     * Sets whether the server should attempt to create a destination portal or
     * not.
     *
     * @param canCreatePortal Sets whether there should be a destination portal
     * created
     */
    void setCanCreatePortal(boolean canCreatePortal);

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
    int getCreationRadius();

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
    void setCreationRadius(int creationRadius);

    /**
     * No effect
     *
     * @return no effect
     * @deprecated No effect
     */
    @Override
    @Deprecated(forRemoval = true)
    Set<TeleportFlag.Relative> getRelativeTeleportationFlags();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
