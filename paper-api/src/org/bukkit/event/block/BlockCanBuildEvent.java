/**
 * 
 */
package org.bukkit.event.block;

import org.bukkit.Block;
import org.bukkit.event.Cancellable;

/**
 * @author durron597
 */
public class BlockCanBuildEvent extends BlockEvent implements Cancellable {
	protected boolean cancel;
	
	public BlockCanBuildEvent(Type type, Block block, boolean canBuild) {
		super(type, block);
		
		cancel = canBuild;
	}

	/**
	 * Returns whether or not the block can be built here. By default, returns
	 * Minecraft's answer on whether the block can be built
	 * 
	 * @return boolean whether or not the block can be built
	 */
	@Override
	public boolean isCancelled() {
		return cancel;
	}
	
	/**
	 * Set whether the block can be built here.
	 */
	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}
}
