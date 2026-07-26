package org.bukkit.block.data.type;

import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Waterlogged;

public interface DecoratedPot extends Directional, Waterlogged {

    /**
     * Gets the value of the 'cracked' property.
     *
     * @return the 'cracked' value
     */
    boolean isCracked();

    /**
     * Sets the value of the 'cracked' property.
     *
     * @param cracked the new 'cracked' value
     */
    void setCracked(boolean cracked);
}
