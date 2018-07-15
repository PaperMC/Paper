package org.bukkit.block.data.type;

import org.bukkit.block.data.Directional;

/**
 * 'eye' denotes whether this end portal frame has been activated by having an
 * eye of ender placed in it.
 */
public interface EndPortalFrame extends Directional {

    /**
     * Gets the value of the 'eye' property.
     *
     * @return the 'eye' value
     */
    boolean hasEye();

    /**
     * Sets the value of the 'eye' property.
     *
     * @param eye the new 'eye' value
     */
    void setEye(boolean eye);
}
