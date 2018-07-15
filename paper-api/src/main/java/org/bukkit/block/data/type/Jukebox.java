package org.bukkit.block.data.type;

import org.bukkit.block.data.BlockData;

/**
 * 'has_record' is a quick flag to check whether this jukebox has a record
 * inside it.
 */
public interface Jukebox extends BlockData {

    /**
     * Gets the value of the 'has_record' property.
     *
     * @return the 'has_record' value
     */
    boolean hasRecord();
}
