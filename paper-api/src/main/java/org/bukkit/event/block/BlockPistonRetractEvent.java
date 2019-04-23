package org.bukkit.event.block;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a piston retracts
 */
public class BlockPistonRetractEvent extends BlockPistonEvent {
    private static final HandlerList handlers = new HandlerList();
    private List<Block> blocks;

    public BlockPistonRetractEvent(@NotNull final Block block, @NotNull final List<Block> blocks, @NotNull final BlockFace direction) {
        super(block, direction);

        this.blocks = blocks;
    }

    /**
     * Gets the location where the possible moving block might be if the
     * retracting piston is sticky.
     *
     * @return The possible location of the possibly moving block.
     */
    @Deprecated
    @NotNull
    public Location getRetractLocation() {
        return getBlock().getRelative(getDirection(), 2).getLocation();
    }

    /**
     * Get an immutable list of the blocks which will be moved by the
     * extending.
     *
     * @return Immutable list of the moved blocks.
     */
    @NotNull
    public List<Block> getBlocks() {
        return blocks;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
