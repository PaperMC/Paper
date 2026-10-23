package io.papermc.paper.event.inventory;

import org.bukkit.inventory.Inventory;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PaperPrepareHopperTransferEvent extends PrepareHopperTransferEvent {

    public PaperPrepareHopperTransferEvent(final Inventory source, final Inventory destination, final boolean didSourceInitiate) {
        super(source, destination, didSourceInitiate);
    }

    public long getDisallowedSlots() {
        return this.disallowedSlots;
    }
}
