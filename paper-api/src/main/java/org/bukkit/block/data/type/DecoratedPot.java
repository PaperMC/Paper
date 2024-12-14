package org.bukkit.block.data.type;

import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Waterlogged;

/**
 * @since 1.19.4
 */
public interface DecoratedPot extends Directional, Waterlogged {
    // Paper start - add missing block data api
    /**
     * @return whether the pot is cracked
     * @since 1.21.1
     */
    public boolean isCracked();

    /**
     * Set whether the pot is cracked.
     *
     * @param cracked whether the pot is cracked
     * @since 1.21.1
     */
    public void setCracked(boolean cracked);
    // Paper end - add missing block data api
}
