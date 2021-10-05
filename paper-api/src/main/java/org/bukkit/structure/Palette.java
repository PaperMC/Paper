package org.bukkit.structure;

import java.util.List;
import org.bukkit.block.BlockState;
import org.jetbrains.annotations.NotNull;

/**
 * Represent a variation of a structure.
 *
 * Most structures, like the ones generated with structure blocks, only have a
 * single variant.
 */
public interface Palette {

    /**
     * Gets a copy of the blocks this Palette is made of.
     *
     * The {@link BlockState#getLocation() positions} of the returned block
     * states are offsets relative to the structure's position that is provided
     * once the structure is placed into the world.
     *
     * @return The blocks in this palette
     */
    @NotNull
    List<BlockState> getBlocks();

    /**
     * Gets the number of blocks stored in this palette.
     *
     * @return The number of blocks in this palette
     */
    int getBlockCount();
}
