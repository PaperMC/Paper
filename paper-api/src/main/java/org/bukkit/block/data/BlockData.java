package org.bukkit.block.data;

import org.bukkit.Material;
import org.bukkit.Server;

public interface BlockData extends Cloneable {

    /**
     * Get the Material represented by this block data.
     *
     * @return the material
     */
    Material getMaterial();

    /**
     * Gets a string, which when passed into a method such as
     * {@link Server#createBlockData(java.lang.String)} will unambiguously
     * recreate this instance.
     *
     * @return serialized data string for this block
     */
    String getAsString();

    /**
     * Returns a copy of this BlockData.
     *
     * @return a copy of the block data
     */
    BlockData clone();
}
