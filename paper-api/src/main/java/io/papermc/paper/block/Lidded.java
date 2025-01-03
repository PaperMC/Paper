package io.papermc.paper.block;

import org.bukkit.block.TileState;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface Lidded extends TileState {

    /**
     * Gets the current state of the block, respecting the lidded mode.
     *
     * @return the effective lid state
     */
    LidState getEffectiveLidState();

    /**
     * Gets how the lid would be without any lidded mode, based on players interacting with the block.
     * @return the true lid state
     */
    LidState getTrueLidState();

    /**
     * Gets the current lid mode of the block.
     *
     * @return the lid mode
     */
    LidMode getLidMode();

    /**
     * Sets the lid mode of the block.
     *
     * @param mode the new lid mode
     * @return the actually set lid mode
     */
    LidMode setLidMode(LidMode mode);

}
