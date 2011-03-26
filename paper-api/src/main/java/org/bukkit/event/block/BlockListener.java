package org.bukkit.event.block;

import org.bukkit.event.Listener;

/**
 * Handles all events thrown in relation to Blocks
 *
 * @author durron597
 */
public class BlockListener implements Listener {
    /**
     * Default Constructor
     */
    public BlockListener() {
    }

    /**
     * Called when a block is damaged (or broken)
     *
     * @param event Relevant event details
     */
    public void onBlockDamage(BlockDamageEvent event) {
    }

    /**
     * Called when we try to place a block, to see if we can build it
     */
    public void onBlockCanBuild(BlockCanBuildEvent event) {
    }

    /**
     * Called when a block flows (water/lava)
     *
     * @param event Relevant event details
     */
    public void onBlockFromTo(BlockFromToEvent event) {
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
    public void onBlockPlace(BlockPlaceEvent event) {
    }

    /**
     * Called when redstone changes
     * From: the source of the redstone change
     * To: The redstone dust that changed
     *
     * @param event Relevant event details
     */
    public void onBlockRedstoneChange(BlockRedstoneEvent event) {
    }

    /**
     * Called when leaves are decaying naturally
     *
     * @param event Relevant event details
     */
    public void onLeavesDecay(LeavesDecayEvent event) {
    }

    /**
     * Called when a sign is changed
     *
     * @param event Relevant event details
     */
    public void onSignChange(SignChangeEvent event) {
    }

    /**
     * Called when a block is destroyed from burning
     *
     * @param event Relevant event details
     */
    public void onBlockBurn(BlockBurnEvent event) {
    }

    /**
     * Called when a block is destroyed by a player.
     *
     * @param event Relevant event details
     */
    public void onBlockBreak(BlockBreakEvent event) {
    }
}
