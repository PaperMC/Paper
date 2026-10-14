package org.bukkit.block.data.type;

import org.bukkit.block.data.Directional;
import org.bukkit.block.data.FaceAttachable;
import org.bukkit.block.data.Powerable;
import org.jetbrains.annotations.NotNull;

public interface Switch extends Directional, FaceAttachable, Powerable {

    @NotNull
    AttachedFace getAttachedFace();

    void setAttachedFace(@NotNull AttachedFace face);

    /**
     * Gets the value of the 'face' property.
     *
     * @return the 'face' value
     * @deprecated use {@link #getAttachedFace()}
     */
    @NotNull
    @Deprecated(since = "1.15.2")
    default Face getFace() {
        return Face.valueOf(this.getAttachedFace().name());
    }

    /**
     * Sets the value of the 'face' property.
     *
     * @param face the new 'face' value
     * @deprecated use {@link #setAttachedFace(AttachedFace)}
     */
    @Deprecated(since = "1.15.2")
    default void setFace(@NotNull Face face) {
        this.setAttachedFace(AttachedFace.valueOf(face.name()));
    }

    /**
     * The face to which a switch type block is stuck.
     *
     * @deprecated use {@link AttachedFace}
     */
    @Deprecated(since = "1.15.2")
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
         * The switch is mounted to the ceiling and pointing downwards.
         */
        CEILING;
    }
}
