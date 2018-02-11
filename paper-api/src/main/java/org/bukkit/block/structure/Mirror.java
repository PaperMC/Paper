package org.bukkit.block.structure;

/**
 * Represents how a {@link org.bukkit.block.Structure} can be mirrored upon
 * being loaded.
 */
public enum Mirror {

    /**
     * No mirroring.
     * <br>
     * Positive X to Positive Z
     */
    NONE,
    /**
     * Structure is mirrored left to right.
     * <br>
     * Similar to looking in a mirror. Positive X to Negative Z
     */
    LEFT_RIGHT,
    /**
     * Structure is mirrored front to back.
     * <br>
     * Positive Z to Negative X
     */
    FRONT_BACK;
}
