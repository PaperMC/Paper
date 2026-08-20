package io.papermc.paper.event.block;

import org.bukkit.block.Beacon;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEventNew;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Called when a beacon is deactivated, either because its base block(s) or itself were destroyed.
 */
@NullMarked
public interface BeaconDeactivatedEvent extends BlockEventNew {

    /**
     * Returns the beacon that was deactivated.
     * This will return {@code null} if the beacon does not exist.
     * (which can occur after the deactivation of a now broken beacon)
     *
     * @return The beacon that got deactivated, or {@code null} if it does not exist.
     */
    @Nullable Beacon getBeacon();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
