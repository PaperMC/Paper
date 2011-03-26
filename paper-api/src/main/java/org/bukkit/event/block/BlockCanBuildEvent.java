/**
 *
 */
package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.Material;

/**
 * @author durron597
 */
public class BlockCanBuildEvent extends BlockEvent {
    protected boolean buildable;
    protected int material;

    public BlockCanBuildEvent(Block block, int id, boolean canBuild) {
        super(Type.BLOCK_CANBUILD, block);
        buildable = canBuild;
        material = id;
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
        return Material.getMaterial(material);
    }

    public int getMaterialId() {
        return material;
    }
}
