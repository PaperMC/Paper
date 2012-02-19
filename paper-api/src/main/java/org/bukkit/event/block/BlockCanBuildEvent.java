package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.event.HandlerList;

/**
 * Called when we try to place a block, to see if we can build it here or not.
 * <p />
 * Note:
 * <ul>
 * <li>The Block returned by getBlock() is the block we are trying to place on, not the block we are trying to place.</li>
 * <li>If you want to figure out what is being placed, use {@link #getMaterial()} or {@link #getMaterialId()} instead.</li>
 * </ul>
 */
public class BlockCanBuildEvent extends BlockEvent {
    private static final HandlerList handlers = new HandlerList();
    protected boolean buildable;
    protected int material;

    public BlockCanBuildEvent(final Block block, final int id, final boolean canBuild) {
        super(block);
        buildable = canBuild;
        material = id;
    }

    /**
     * Gets whether or not the block can be built here.
     * By default, returns Minecraft's answer on whether the block can be built here or not.
     *
     * @return boolean whether or not the block can be built
     */
    public boolean isBuildable() {
        return buildable;
    }

    /**
     * Sets whether the block can be built here or not.
     *
     * @param cancel true if you want to allow the block to be built here despite Minecraft's default behaviour
     */
    public void setBuildable(boolean cancel) {
        this.buildable = cancel;
    }

    /**
     * Gets the Material that we are trying to place.
     *
     * @return The Material that we are trying to place
     */
    public Material getMaterial() {
        return Material.getMaterial(material);
    }

    /**
     * Gets the Material ID for the Material that we are trying to place.
     *
     * @return The Material ID for the Material that we are trying to place
     */
    public int getMaterialId() {
        return material;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
