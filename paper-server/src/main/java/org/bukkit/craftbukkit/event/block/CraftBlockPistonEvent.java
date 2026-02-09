package org.bukkit.craftbukkit.event.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockPistonEvent;

public abstract class CraftBlockPistonEvent extends CraftBlockEvent implements BlockPistonEvent {

    private final BlockFace direction;
    private boolean cancelled;

    protected CraftBlockPistonEvent(final Block block, final BlockFace direction) {
        super(block);
        this.direction = direction;
    }

    @Override
    public boolean isSticky() {
        return this.block.getType() == Material.STICKY_PISTON || this.block.getType() == Material.MOVING_PISTON;
    }

    @Override
    public BlockFace getDirection() {
        // Both are meh!
        // return ((PistonBaseMaterial) block.getType().getNewData(block.getData())).getFacing();
        // return ((PistonBaseMaterial) block.getState().getData()).getFacing();
        return this.direction;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }
}
