package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.Material;

/**
 * Thrown when a block physics check is called
 *
 * @author Dinnerbone
 */
public class BlockPhysicsEvent extends BlockEvent {
    private final int changed;
    private boolean cancel = false;

    public BlockPhysicsEvent(final Block block, final int changed) {
        super(Type.BLOCK_PHYSICS, block);
        this.changed = changed;
    }

    /**
     * Gets the type of block that changed, causing this event
     *
     * @return Changed block's type id
     */
    public int getChangedTypeId() {
        return changed;
    }

    /**
     * Gets the type of block that changed, causing this event
     *
     * @return Changed block's type
     */
    public Material getChangedType() {
        return Material.getMaterial(changed);
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
