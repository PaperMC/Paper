package org.bukkit.block.data;

/**
 * 'open' denotes whether this block is currently opened.
 */
public interface Openable extends BlockData {

    /**
     * Gets the value of the 'open' property.
     *
     * @return the 'open' value
     */
    boolean isOpen();

    /**
     * Sets the value of the 'open' property.
     *
     * @param open the new 'open' value
     */
    void setOpen(boolean open);
}
