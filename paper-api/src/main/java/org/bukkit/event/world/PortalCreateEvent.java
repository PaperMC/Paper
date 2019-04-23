package org.bukkit.event.world;

import java.util.ArrayList;
import java.util.Collection;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a portal is created
 */
public class PortalCreateEvent extends WorldEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private final ArrayList<Block> blocks = new ArrayList<Block>();
    private CreateReason reason = CreateReason.FIRE;

    public PortalCreateEvent(@NotNull final Collection<Block> blocks, @NotNull final World world, @NotNull CreateReason reason) {
        super(world);

        this.blocks.addAll(blocks);
        this.reason = reason;
    }

    /**
     * Gets an array list of all the blocks associated with the created portal
     *
     * @return array list of all the blocks associated with the created portal
     */
    @NotNull
    public ArrayList<Block> getBlocks() {
        return this.blocks;
    }

    public boolean isCancelled() {
        return cancel;
    }

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
         * When a portal is created 'traditionally' due to a portal frame
         * being set on fire.
         */
        FIRE,
        /**
         * When a portal is created as a destination for an existing portal
         * when using the custom PortalTravelAgent
         */
        OBC_DESTINATION
    }
}
