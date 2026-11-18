package com.destroystokyo.paper.block;

import org.bukkit.FluidCollisionMode;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 * Represents information about a targeted block.
 *
 * @deprecated use {@link org.bukkit.util.RayTraceResult}
 */
@Deprecated(forRemoval = true, since = "1.19.3")
public class TargetBlockInfo {
    private final Block block;
    private final BlockFace blockFace;

    public TargetBlockInfo(final Block block, final BlockFace blockFace) {
        this.block = block;
        this.blockFace = blockFace;
    }

    /**
     * Get the block that is targeted.
     *
     * @return Targeted block
     */
    public Block getBlock() {
        return this.block;
    }

    /**
     * Get the targeted BlockFace.
     *
     * @return Targeted blockface
     */
    public BlockFace getBlockFace() {
        return this.blockFace;
    }

    /**
     * Get the relative Block to the targeted block on the side it is targeted at.
     *
     * @return Block relative to targeted block
     */
    public Block getRelativeBlock() {
        return this.block.getRelative(this.blockFace);
    }

    /**
     * Determines whether fluids are considered when targeting a block.
     *
     * @deprecated use {@link org.bukkit.FluidCollisionMode}
     */
    @Deprecated(forRemoval = true, since = "1.19.3")
    public enum FluidMode {
        NEVER(FluidCollisionMode.NEVER),
        SOURCE_ONLY(FluidCollisionMode.SOURCE_ONLY),
        ALWAYS(FluidCollisionMode.ALWAYS);

        public final FluidCollisionMode bukkit;

        FluidMode(final FluidCollisionMode bukkit) {
            this.bukkit = bukkit;
        }
    }
}
