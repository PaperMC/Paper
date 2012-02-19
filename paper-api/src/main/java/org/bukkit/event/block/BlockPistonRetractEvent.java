package org.bukkit.event.block;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.HandlerList;

public class BlockPistonRetractEvent extends BlockPistonEvent {
    private static final HandlerList handlers = new HandlerList();
    public BlockPistonRetractEvent(final Block block, final BlockFace direction) {
        super(block, direction);
    }

    /**
     * Gets the location where the possible moving block might be if the retracting
     * piston is sticky.
     *
     * @return The possible location of the possibly moving block.
     */
    public Location getRetractLocation() {
        return getBlock().getRelative(getDirection(), 2).getLocation();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
