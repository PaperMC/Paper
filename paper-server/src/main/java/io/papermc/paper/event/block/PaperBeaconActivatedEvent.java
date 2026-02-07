package io.papermc.paper.event.block;

import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.event.block.CraftBlockEvent;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

public class PaperBeaconActivatedEvent extends CraftBlockEvent implements BeaconActivatedEvent {

    private @Nullable Beacon beacon;

    public PaperBeaconActivatedEvent(final Block beacon) {
        super(beacon);
    }

    @Override
    public Beacon getBeacon() {
        if (this.beacon == null) {
            this.beacon = (Beacon) this.block.getState();
        }
        return this.beacon;
    }

    @Override
    public HandlerList getHandlers() {
        return BeaconActivatedEvent.getHandlerList();
    }
}
