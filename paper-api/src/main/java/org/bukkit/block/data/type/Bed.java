package org.bukkit.block.data.type;

import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Directional;
import org.jetbrains.annotations.NotNull;

/**
 * Similar to {@link Bisected}, 'part' denotes which half of the bed this block
 * corresponds to.
 * <br>
 * 'occupied' property is a quick flag to check if a player is currently
 * sleeping in this bed block.
 */
public interface Bed extends Directional {

    /**
     * Gets the value of the 'part' property.
     *
     * @return the 'part' value
     */
    @NotNull
    Part getPart();

    /**
     * Sets the value of the 'part' property.
     *
     * @param part the new 'part' value
     */
    void setPart(@NotNull Part part);

    /**
     * Gets the value of the 'occupied' property.
     *
     * @return the 'occupied' value
     */
    boolean isOccupied();

    /**
     * Horizontal half of a bed.
     */
    public enum Part {

        /**
         * The head is the upper part of the bed containing the pillow.
         */
        HEAD,
        /**
         * The foot is the lower half of the bed.
         */
        FOOT;
    }
}
