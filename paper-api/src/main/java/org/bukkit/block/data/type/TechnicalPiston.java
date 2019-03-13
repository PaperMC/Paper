package org.bukkit.block.data.type;

import org.bukkit.block.data.Directional;
import org.jetbrains.annotations.NotNull;

/**
 * 'type' represents the type of piston which this (technical) block corresponds
 * to.
 */
public interface TechnicalPiston extends Directional {

    /**
     * Gets the value of the 'type' property.
     *
     * @return the 'type' value
     */
    @NotNull
    Type getType();

    /**
     * Sets the value of the 'type' property.
     *
     * @param type the new 'type' value
     */
    void setType(@NotNull Type type);

    /**
     * Different piston variants.
     */
    public enum Type {
        /**
         * A normal piston which does not pull connected blocks backwards on
         * retraction.
         */
        NORMAL,
        /**
         * A sticky piston which will also retract connected blocks.
         */
        STICKY;
    }
}
