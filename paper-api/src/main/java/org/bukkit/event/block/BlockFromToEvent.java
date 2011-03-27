package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Cancellable;

/**
 * Holds information for events with a source block and a destination block
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
     * Gets the location this player moved to
     *
     * @return Block the block is event originated from
     */
    public BlockFace getFace() {
        return face;
    }

    /**
     * Convenience method for getting the faced block
     * 
     * @return Block the faced block
     */
    public Block getToBlock() {
        if (to == null) {
            to = block.getRelative(face.getModX(), face.getModY(), face.getModZ());
        }
        return to;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
