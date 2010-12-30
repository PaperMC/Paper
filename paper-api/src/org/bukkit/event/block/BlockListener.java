package org.bukkit.event.block;

import org.bukkit.event.Listener;

/**
 * Handles all events thrown in relation to a Block
 * 
 * @author durron597
 */
public class BlockListener implements Listener {
    public BlockListener() {
    }
    
    /**
     * Called when a block is broken (or destroyed)
     *
     * @param event Relevant event details
     */
    public void onBlockBroken(BlockBrokenEvent event) {
    }

    /**
     * Called when a block flows (water/lava)
     *
     * @param event Relevant event details
     */
    public void onBlockFlow(BlockFromToEvent event) {
    }

    /**
     * Called when a block gets ignited
     *
     * @param event Relevant event details
     */
    public void onBlockIgnite(BlockIgniteEvent event) {
    }
    
    /**
     * Called when block physics occurs
     *
     * @param event Relevant event details
     */
    public void onBlockPhysics(BlockPhysicsEvent event) {
    }
    
    /**
     * Called when a player places a block
     *
     * @param event Relevant event details
     */
    public void onBlockPlaced(BlockPlacedEvent event) {
    }

    /**
     * Called when a player right clicks a block
     *
     * @param event Relevant event details
     */
    public void onBlockRightClicked(BlockRightClickedEvent event) {
    }

    /**
     * Called when redstone changes
     * From: the source of the redstone change
     * To: The redstone dust that changed
     *
     * @param event Relevant event details
     */
    public void onRedstoneChange(BlockFromToEvent event) {
    }
}
