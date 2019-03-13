package org.bukkit.block.data.type;

import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Powerable;
import org.jetbrains.annotations.NotNull;

/**
 * 'face' represents the face to which a lever or button is stuck.
 * <br>
 * This is used in conjunction with {@link Directional} to compute the
 * orientation of these blocks.
 */
public interface Switch extends Directional, Powerable {

    /**
     * Gets the value of the 'face' property.
     *
     * @return the 'face' value
     */
    @NotNull
    Face getFace();

    /**
     * Sets the value of the 'face' property.
     *
     * @param face the new 'face' value
     */
    void setFace(@NotNull Face face);

    /**
     * The face to which a switch type block is stuck.
     */
    public enum Face {
        /**
         * The switch is mounted to the floor and pointing upwards.
         */
        FLOOR,
        /**
         * The switch is mounted to the wall.
         */
        WALL,
        /**
         * The switch is mounted to the ceiling and pointing dowanrds.
         */
        CEILING;
    }
}
