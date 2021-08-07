package io.papermc.paper.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when an item is about to be composted by a hopper.
 * To prevent hoppers from moving items into composters, cancel the {@link InventoryMoveItemEvent}.
 */
@NullMarked
public class CompostItemEvent extends BlockEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final ItemStack item;
    private boolean willRaiseLevel;

    @ApiStatus.Internal
    public CompostItemEvent(final Block composter, final ItemStack item, final boolean willRaiseLevel) {
        super(composter);
        this.item = item;
        this.willRaiseLevel = willRaiseLevel;
    }

    /**
     * Gets the item that was used on the composter.
     *
     * @return the item
     */
    public ItemStack getItem() {
        return this.item;
    }

    /**
     * Gets whether the composter will rise a level.
     *
     * @return {@code true} if successful
     */
    public boolean willRaiseLevel() {
        return this.willRaiseLevel;
    }

    /**
     * Sets whether the composter will rise a level.
     *
     * @param willRaiseLevel {@code true} if the composter should rise a level
     */
    public void setWillRaiseLevel(final boolean willRaiseLevel) {
        this.willRaiseLevel = willRaiseLevel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

}
