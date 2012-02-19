package org.bukkit.event.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Cancellable;

public abstract class BlockPistonEvent extends BlockEvent implements Cancellable {
    private boolean cancelled;
    private final BlockFace direction;

    public BlockPistonEvent(final Block block, final BlockFace direction) {
        super(block);
        this.direction = direction;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Returns true if the Piston in the event is sticky.
     *
     * @return stickiness of the piston
     */
    public boolean isSticky() {
        return block.getType() == Material.PISTON_STICKY_BASE;
    }

    /**
     * Return the direction in which the piston will operate.
     *
     * @return direction of the piston
     */
    public BlockFace getDirection() {
        // Both are meh!
        // return ((PistonBaseMaterial) block.getType().getNewData(block.getData())).getFacing();
        // return ((PistonBaseMaterial) block.getState().getData()).getFacing();
        return direction;
    }
}
