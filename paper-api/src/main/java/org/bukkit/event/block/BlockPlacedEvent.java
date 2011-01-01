package org.bukkit.event.block;

import org.bukkit.Block;
import org.bukkit.event.Cancellable;

/**
 * Not implemented yet
 */
public class BlockPlacedEvent extends BlockEvent implements Cancellable {
	private boolean cancel;
	
	/**
	 * @param type
	 * @param theBlock
	 */
	public BlockPlacedEvent(Type type, Block theBlock) {
		super(type, theBlock);
		cancel = false;
	}

	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return cancel;
	}

	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

}
