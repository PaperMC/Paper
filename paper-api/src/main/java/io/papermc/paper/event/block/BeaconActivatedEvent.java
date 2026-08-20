package io.papermc.paper.event.block;

import org.bukkit.block.Beacon;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEventNew;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a beacon is activated.
 * Activation occurs when the beacon beam becomes visible.
 */
@NullMarked
public interface BeaconActivatedEvent extends BlockEventNew {

    /**
     * Returns the beacon that was activated.
     *
     * @return the beacon that was activated.
     */
    Beacon getBeacon();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
