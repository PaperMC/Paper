package io.papermc.paper.event.entity;

import org.bukkit.PortalType;
import org.bukkit.World;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEventNew;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Called when an entity is ready to be teleported by a plugin.
 * Currently, this is only called after the required
 * ticks have passed for a Nether Portal.
 * <p>
 * Cancelling this event resets the entity's readiness
 * regarding the current portal.
 */
@NullMarked
public interface EntityPortalReadyEvent extends EntityEventNew, Cancellable {

    /**
     * Gets the world this portal will teleport to.
     * Can be {@code null} if "allow-nether" is false in server.properties
     * or if there is another situation where there is no world to teleport to.
     * <p>
     * This world may be modified by later events such as {@link PlayerPortalEvent}
     * or {@link EntityPortalEvent}.
     *
     * @return the world the portal will teleport the entity to.
     */
    @Nullable World getTargetWorld();

    /**
     * Sets the world this portal will teleport to. A {@code null} value
     * will essentially cancel the teleport and prevent further events
     * such as {@link PlayerPortalEvent} from firing.
     * <p>
     * This world may be modified by later events such as {@link PlayerPortalEvent}
     * or {@link EntityPortalEvent}.
     *
     * @param targetWorld the world
     */
    void setTargetWorld(@Nullable World targetWorld);

    /**
     * Gets the portal type for this event.
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
