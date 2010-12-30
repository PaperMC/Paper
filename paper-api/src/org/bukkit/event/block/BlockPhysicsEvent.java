
package org.bukkit.event.block;

import org.bukkit.Block;
import org.bukkit.ItemStack;
import org.bukkit.event.Event;

/**
 * Thrown when a block physics check is called
 */
public class BlockPhysicsEvent extends BlockEvent {
    private final Block block;
    private final int changed;
    private boolean cancel = false;

    public BlockPhysicsEvent(final Event.Type type, final Block block, final int changed) {
        super(type);
        this.block = block;
        this.changed = changed;
    }

    /**
     * Gets the block currently undergoing a physics check
     * 
     * @return Block to check physics on
     */
    public Block getBlock() {
        return block;
    }

    /**
     * Gets the type of block that changed, causing this event
     *
     * @return Changed block's type ID
     */
    public int getChangedTypeID() {
        return changed;
    }

    /**
     * Gets the type of block that changed, causing this event
     *
     * @return Changed block's type
     */
    public ItemStack.Type getChangedType() {
        return ItemStack.Type.getType(changed); // TODO: Move type to its own file
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @param cancel true if you wish to cancel this event
     */
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
