package org.bukkit.block.data.type;

import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Powerable;

/**
 * Similar to {@link Powerable}, 'triggered' indicates whether or not the
 * dispenser is currently activated.
 */
public interface Dispenser extends Directional {

    /**
     * Gets the value of the 'triggered' property.
     *
     * @return the 'triggered' value
     */
    boolean isTriggered();

    /**
     * Sets the value of the 'triggered' property.
     *
     * @param triggered the new 'triggered' value
     */
    void setTriggered(boolean triggered);
}
