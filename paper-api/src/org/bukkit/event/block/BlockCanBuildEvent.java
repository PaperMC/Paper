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
	
	public BlockCanBuildEvent(Type type, Block block) {
		super(type, block);
	}
	
	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}
}
