package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Cancellable;

/**
 * Represents events with a source block and a destination block, currently only applies to liquid (lava and water).
 *<p />
 * If a Block From To event is cancelled, the block will not move (the liquid will not flow).
 */
public class BlockFromToEvent extends BlockEvent implements Cancellable {
    protected Block to;
    protected BlockFace face;
    protected boolean cancel;

    public BlockFromToEvent(final Block block, final BlockFace face) {
        super(Type.BLOCK_FROMTO, block);
        this.face = face;
        this.cancel = false;
    }

    /**
     * Gets the BlockFace that the block is moving to.
     *
     * @return The BlockFace that the block is moving to
     */
    public BlockFace getFace() {
        return face;
    }

    /**
     * Convenience method for getting the faced Block.
     *
     * @return The faced Block
     */
    public Block getToBlock() {
        if (to == null) {
            to = block.getRelative(face.getModX(), face.getModY(), face.getModZ());
        }
        return to;
    }

     /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins.
     *<p />
     * If a Block From To event is cancelled, the block will not move.
     *
     * @return true if this event is cancelled
     */
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins.
     *<p />
     * If a Block From To event is cancelled, the block will not move.
     *
     * @param cancel true if you wish to cancel this event
     */
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
