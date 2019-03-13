package org.bukkit.block.data.type;

import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.Powerable;
import org.jetbrains.annotations.NotNull;

/**
 * 'hinge' indicates which hinge this door is attached to and will rotate around
 * when opened.
 */
public interface Door extends Bisected, Directional, Openable, Powerable {

    /**
     * Gets the value of the 'hinge' property.
     *
     * @return the 'hinge' value
     */
    @NotNull
    Hinge getHinge();

    /**
     * Sets the value of the 'hinge' property.
     *
     * @param hinge the new 'hinge' value
     */
    void setHinge(@NotNull Hinge hinge);

    /**
     * The hinge of a door.
     */
    public enum Hinge {
        /**
         * Door is attached to the left side.
         */
        LEFT,
        /**
         * Door is attached to the right side.
         */
        RIGHT;
    }
}
