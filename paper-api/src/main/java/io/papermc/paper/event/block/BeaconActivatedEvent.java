package io.papermc.paper.event.block;

import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a beacon is activated.
 * Activation occurs when the beacon beam becomes visible.
 */
@NullMarked
public class BeaconActivatedEvent extends BlockEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    @ApiStatus.Internal
    public BeaconActivatedEvent(final Block beacon) {
        super(beacon);
    }

    /**
     * Returns the beacon that was activated.
     *
     * @return the beacon that was activated.
     */
    public Beacon getBeacon() {
        return (Beacon) this.block.getState();
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
