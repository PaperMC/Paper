package org.bukkit.event.world;

import java.util.List;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

/**
 * Called when a portal is created
 */
public interface PortalCreateEvent extends WorldEvent, Cancellable {

    /**
     * Gets an array list of all the blocks associated with the created portal
     *
     * @return array list of all the blocks associated with the created portal
     */
    List<BlockState> getBlocks();

    /**
     * Returns the Entity that triggered this portal creation (if available)
     *
     * @return Entity involved in this event
     */
    @Nullable Entity getEntity();

    /**
     * Gets the reason for the portal's creation
     *
     * @return CreateReason for the portal's creation
     */
    CreateReason getReason();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    /**
     * An enum to specify the various reasons for a portal's creation
     */
    enum CreateReason {
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
