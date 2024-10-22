package org.bukkit.block.data.type;

import org.bukkit.MinecraftExperimental;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.ApiStatus;

/**
 * 'tip' indicates whether this block is a tip.
 */
@ApiStatus.Experimental
@MinecraftExperimental(MinecraftExperimental.Requires.WINTER_DROP)
public interface HangingMoss extends BlockData {

    /**
     * Gets the value of the 'tip' property.
     *
     * @return the 'tip' value
     */
    boolean isTip();

    /**
     * Sets the value of the 'tip' property.
     *
     * @param tip the new 'tip' value
     */
    void setTip(boolean tip);
}
