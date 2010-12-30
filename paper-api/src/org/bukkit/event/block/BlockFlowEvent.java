package org.bukkit.event.block;

import java.util.HashSet;
import org.bukkit.Block;
import org.bukkit.BlockFace;
import org.bukkit.event.Event;

/**
 * Holds information for events with a source block and a destination block
 */
public class BlockFlowEvent extends BlockEvent {
    protected HashSet<BlockFlow> faceList;

    public BlockFlowEvent(final Event.Type type, final Block block, BlockFace... faces) {
        super(type, block);
        this.faceList = new HashSet<BlockFlow>();
        if (faces != null && faces.length > 0) {
        	for (BlockFace theFace : faces) {
        		faceList.add(new BlockFlow(theFace));
        	}
        }
    }

    /**
     * Gets the location this player moved to
     *
     * @return Block the block is event originated from
     */
    public HashSet<BlockFlow> getFaces() {
        return faceList;
    }
    
    /**
     * Class that represents a flow direction and whether it's cancelled
     */
    public class BlockFlow {
    	private final BlockFace flowDirection;
    	private boolean cancelled;
    	
    	public BlockFlow(BlockFace flowDirection) {
    		this.flowDirection = flowDirection;
    		cancelled = false;
    	}
    	
    	public BlockFace getFlowDirection() {
    		return flowDirection;
    	}
    	
    	public boolean isCancelled() {
    		return cancelled;
    	}
    	
    	public void setCancelled(boolean cancel) {
    		cancelled = cancel;
    	}
    	
    	@Override
    	public boolean equals(Object o) {
    		if (!(o instanceof BlockFlow)) return false;
    		return equals((BlockFlow) o);
    	}
    	
    	public boolean equals(BlockFlow flow) {
    		return flow.flowDirection.equals(flow);
    	}
    	
    	@Override
    	public int hashCode() {
    		return flowDirection.hashCode();
    	}
    }
}
