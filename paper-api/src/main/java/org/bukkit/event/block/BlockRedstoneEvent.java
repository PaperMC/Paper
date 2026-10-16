package org.bukkit.event.block;

import com.google.common.base.Preconditions;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.checkerframework.common.value.qual.IntRange;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a redstone current changes.
 * <p>
 * It includes the relevant mutation of the {@code powered} and {@code power}
 * properties even if the block is not able to produce a redstone signal.
 * For the {@code powered} property, a high state will be considered as
 * a current of 15 and a low state as 0. Setting the new current to a different
 * value will prevent most action in this case.
 */
@NullMarked
public class BlockRedstoneEvent extends BlockEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final int oldCurrent;
    private int newCurrent;

    @ApiStatus.Internal
    public BlockRedstoneEvent(final Block block, final int oldCurrent, final int newCurrent) {
        super(block);
        this.oldCurrent = oldCurrent;
        this.newCurrent = newCurrent;
    }

    /**
     * Gets the old current of this block.
     *
     * @return the previous current
     */
    public @IntRange(from = 0, to = 15) int getOldCurrent() {
        return this.oldCurrent;
    }

    /**
     * Gets the new current of this block.
     *
     * @return the new current
     */
    public @IntRange(from = 0, to = 15) int getNewCurrent() {
        return this.newCurrent;
    }

    /**
     * Sets the new current of this block.
     *
     * @param newCurrent the new current to set
     */
    public void setNewCurrent(@IntRange(from = 0, to = 15) int newCurrent) {
        Preconditions.checkArgument(newCurrent >= 0 && newCurrent <= 15, "New current must be a redstone signal between 0 and 15 (was %s)", newCurrent);
        this.newCurrent = newCurrent;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
