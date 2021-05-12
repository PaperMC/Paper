package io.papermc.paper.event.entity;

import org.bukkit.PortalType;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.jetbrains.annotations.ApiStatus;
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
public class EntityPortalReadyEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final PortalType portalType;
    private @Nullable World targetWorld;

    private boolean cancelled;

    @ApiStatus.Internal
    public EntityPortalReadyEvent(final Entity entity, final @Nullable World targetWorld, final PortalType portalType) {
        super(entity);
        this.targetWorld = targetWorld;
        this.portalType = portalType;
    }

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
    public @Nullable World getTargetWorld() {
        return this.targetWorld;
    }

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
    public void setTargetWorld(final @Nullable World targetWorld) {
        this.targetWorld = targetWorld;
    }

    /**
     * Gets the portal type for this event.
     *
     * @return the portal type
     */
    public PortalType getPortalType() {
        return this.portalType;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
