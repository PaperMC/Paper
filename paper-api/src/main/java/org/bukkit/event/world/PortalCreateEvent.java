package org.bukkit.event.world;

import java.util.List;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a portal is created
 */
public class PortalCreateEvent extends WorldEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private final List<BlockState> blocks;
    private final Entity entity;
    private final CreateReason reason;

    @Deprecated
    public PortalCreateEvent(@NotNull final List<BlockState> blocks, @NotNull final World world, @NotNull CreateReason reason) {
        this(blocks, world, null, reason);
    }

    public PortalCreateEvent(@NotNull final List<BlockState> blocks, @NotNull final World world, @Nullable Entity entity, @NotNull CreateReason reason) {
        super(world);

        this.blocks = blocks;
        this.entity = entity;
        this.reason = reason;
    }

    /**
     * Gets an array list of all the blocks associated with the created portal
     *
     * @return array list of all the blocks associated with the created portal
     */
    @NotNull
    public List<BlockState> getBlocks() {
        return this.blocks;
    }

    /**
     * Returns the Entity that triggered this portal creation (if available)
     *
     * @return Entity involved in this event
     */
    @Nullable
    public Entity getEntity() {
        return entity;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Gets the reason for the portal's creation
     *
     * @return CreateReason for the portal's creation
     */
    @NotNull
    public CreateReason getReason() {
        return reason;
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

    /**
     * An enum to specify the various reasons for a portal's creation
     */
    public enum CreateReason {
        /**
         * When the blocks inside a portal are created due to a portal frame
         * being set on fire.
         */
        FIRE,
        /**
         * When a nether portal frame and portal is created at the exit of an
         * entered nether portal.
         */
        NETHER_PAIR,
        /**
         * When the target end platform is created as a result of a player
         * entering an end portal.
         */
        END_PLATFORM
    }
}
