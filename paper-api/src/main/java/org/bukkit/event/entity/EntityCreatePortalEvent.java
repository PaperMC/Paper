package org.bukkit.event.entity;

import java.util.List;
import org.bukkit.PortalType;
import org.bukkit.block.BlockState;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.PortalCreateEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Thrown when a Living Entity creates a portal in a world.
 *
 * @deprecated Use {@link PortalCreateEvent}
 */
@Deprecated(since = "1.14.1")
public class EntityCreatePortalEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final List<BlockState> blocks;
    private final PortalType type;

    private boolean cancelled;

    @ApiStatus.Internal
    public EntityCreatePortalEvent(@NotNull final LivingEntity livingEntity, @NotNull final List<BlockState> blocks, @NotNull final PortalType type) {
        super(livingEntity);

        this.blocks = blocks;
        this.type = type;
    }

    @NotNull
    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }

    /**
     * Gets a list of all blocks associated with the portal.
     *
     * @return List of blocks that will be changed.
     */
    @NotNull
    public List<BlockState> getBlocks() {
        return this.blocks;
    }

    /**
     * Gets the type of portal that is trying to be created.
     *
     * @return Type of portal.
     */
    @NotNull
    public PortalType getPortalType() {
        return this.type;
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
