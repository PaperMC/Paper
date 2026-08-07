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
    private int raisedLevels;

    @ApiStatus.Internal
    public CompostItemEvent(final Block composter, final ItemStack item, final int raisedLevels) {
        super(composter);
        this.item = item;
        this.raisedLevels = raisedLevels;
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
     * @deprecated items can now control how many layers they add on click. Use {@link #getLevelsToRaise()}.
     */
    @Deprecated(since = "26.3", forRemoval = true)
    public boolean willRaiseLevel() {
        return this.raisedLevels > 0;
    }

    /**
     * Sets whether the composter will rise a level.
     *
     * @param willRaiseLevel {@code true} if the composter should rise a level. Use {@link #setLevelsToRaise(int)}.
     */
    @Deprecated(since = "26.3", forRemoval = true)
    public void setWillRaiseLevel(final boolean willRaiseLevel) {
        this.raisedLevels = willRaiseLevel ? Math.max(1, this.raisedLevels) : 0;
    }

    /**
     * {@return the levels the composter will be raised by}
     */
    public int getLevelsToRaise() {
        return this.raisedLevels;
    }

    /**
     * Configures the levels the composter will be raised by.
     *
     * @param raisedLevels the levels to raise the composter by.
     */
    public void setLevelsToRaise(final int raisedLevels) {
        this.raisedLevels = raisedLevels;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

}
