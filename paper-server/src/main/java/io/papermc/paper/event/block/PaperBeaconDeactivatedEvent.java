package io.papermc.paper.event.block;

import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.event.block.CraftBlockEvent;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

public class PaperBeaconDeactivatedEvent extends CraftBlockEvent implements BeaconDeactivatedEvent {

    private @Nullable Beacon beacon;

    public PaperBeaconDeactivatedEvent(final Block beacon) {
        super(beacon);
    }

    @Override
    public @Nullable Beacon getBeacon() {
        if (this.beacon == null) {
            this.beacon = this.block.getType() == Material.BEACON ? (Beacon) this.block.getState() : null;
        }
        return this.beacon;
    }

    @Override
    public HandlerList getHandlers() {
        return BeaconDeactivatedEvent.getHandlerList();
    }
}
