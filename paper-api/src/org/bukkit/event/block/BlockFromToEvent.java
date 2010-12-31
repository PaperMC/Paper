package org.bukkit.event.block;

import org.bukkit.Block;
import org.bukkit.BlockFace;
import org.bukkit.event.Event;

/**
 * Holds information for events with a source block and a destination block
 */
public class BlockFromToEvent extends BlockEvent {
    protected Block from;
	protected BlockFace face;

    public BlockFromToEvent(final Event.Type type, final Block block, final BlockFace face) {
        super(type, block);
        this.face = face;
        this.from = block.getRelative(face.getModX(), face.getModY(), face.getModZ());
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
    public Block getFromBlock() {
    	return from; 
    }
}
