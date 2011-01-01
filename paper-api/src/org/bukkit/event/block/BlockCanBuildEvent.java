/**
 * 
 */
package org.bukkit.event.block;

import org.bukkit.Block;
import org.bukkit.Material;
import org.bukkit.event.Cancellable;

/**
 * @author durron597
 */
public class BlockCanBuildEvent extends BlockEvent {
	protected boolean buildable;
	protected Material material;
	
	public BlockCanBuildEvent(Type type, Block block, Material mat, boolean canBuild) {
		super(type, block);
		buildable = canBuild;
		material = mat;
	}

	/**
	 * Returns whether or not the block can be built here. By default, returns
	 * Minecraft's answer on whether the block can be built
	 * 
	 * @return boolean whether or not the block can be built
	 */
	public boolean isBuildable() {
		return buildable;
	}
	
	/**
	 * Set whether the block can be built here.
	 */
	public void setBuildable(boolean cancel) {
		this.buildable = cancel;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public int getMaterialID() {
		return material.getID();
	}
}
