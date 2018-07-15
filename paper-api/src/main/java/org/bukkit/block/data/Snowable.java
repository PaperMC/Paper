package org.bukkit.block.data;

/**
 * 'snowy' denotes whether this block has a snow covered side and top texture
 * (normally because the block above is snow).
 */
public interface Snowable extends BlockData {

    /**
     * Gets the value of the 'snowy' property.
     *
     * @return the 'snowy' value
     */
    boolean isSnowy();

    /**
     * Sets the value of the 'snowy' property.
     *
     * @param snowy the new 'snowy' value
     */
    void setSnowy(boolean snowy);
}
