package org.bukkit.block.data.type;

import org.bukkit.block.data.BlockData;

/**
 * 'bloom' indicates whether the sculk catalyst is actively spreading the sculk
 * or not.
 */
public interface SculkCatalyst extends BlockData {

    /**
     * Gets the value of the 'bloom' property.
     *
     * @return the 'bloom' value
     */
    boolean isBloom();

    /**
     * Sets the value of the 'bloom' property.
     *
     * @param bloom the new 'bloom' value
     */
    void setBloom(boolean bloom);
}
