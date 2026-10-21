package io.papermc.paper.event.inventory;

import com.google.common.base.Preconditions;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a hopper or hopper minecart is about to choose an item to move,
 * before any {@link InventoryMoveItemEvent}.
 * <p>
 * The hopper treats disallowed slots of the source as if they do not exist, so
 * a source with items only in disallowed slots is treated as empty.
 */
@NullMarked
public class PrepareHopperTransferEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Inventory source;
    private final Inventory destination;
    private final boolean didSourceInitiate;
    protected long disallowedSlots;

    @ApiStatus.Internal
    public PrepareHopperTransferEvent(final Inventory source, final Inventory destination, final boolean didSourceInitiate) {
        this.source = source;
        this.destination = destination;
        this.didSourceInitiate = didSourceInitiate;
    }

    /**
     * Gets the Inventory that items are being taken from
     *
     * @return Inventory that items are being taken from
     */
    public Inventory getSource() {
        return this.source;
    }

    /**
     * Gets the Inventory that items are being put into
     *
     * @return Inventory that items are being put into
     */
    public Inventory getDestination() {
        return this.destination;
    }

    /**
     * Gets the Inventory that initiated the transfer. This will always be
     * either the destination or source Inventory.
     *
     * @return Inventory that initiated the transfer
     */
    public Inventory getInitiator() {
        return this.didSourceInitiate ? this.source : this.destination;
    }

    /**
     * Gets if the hopper is allowed to take items from a slot of {@link #getSource()}.
     *
     * @param slot the slot
     * @return true if the slot is allowed
     */
    public boolean isSlotAllowed(final int slot) {
        Preconditions.checkArgument(slot >= 0 && slot < this.source.getSize(), "Slot %s is outside the source inventory", slot);
        return (this.disallowedSlots & (1L << slot)) == 0;
    }

    /**
     * Sets if the hopper is allowed to take items from a slot of {@link #getSource()}.
     *
     * @param slot the slot
     * @param allowed whether the slot is allowed
     */
    public void setSlotAllowed(final int slot, final boolean allowed) {
        Preconditions.checkArgument(slot >= 0 && slot < this.source.getSize(), "Slot %s is outside the source inventory", slot);
        if (allowed) {
            this.disallowedSlots &= ~(1L << slot);
        } else {
            this.disallowedSlots |= 1L << slot;
        }
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
