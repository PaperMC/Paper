package org.bukkit.block.data;

import org.jspecify.annotations.NullMarked;

/**
 * 'side_chain' represents the current side of this block.
 */
@NullMarked
public interface SideChaining extends BlockData {

    /**
     * Gets the value of the 'side_chain' property.
     *
     * @return the 'side_chain' value
     */
    ChainPart getSideChain();

    /**
     * Sets the value of the 'side_chain' property.
     *
     * @param part the new 'side_chain' value
     */
    void setSideChain(ChainPart part);

    enum ChainPart {
        UNCONNECTED,
        RIGHT,
        CENTER,
        LEFT
    }
}
