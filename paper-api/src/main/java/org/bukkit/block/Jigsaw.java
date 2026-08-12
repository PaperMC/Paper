package org.bukkit.block;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a captured state of a jigsaw.
 */
public interface Jigsaw extends TileState {

    /**
     * Gets the Target Pool Structure key
     *
     * @return The NamespacedKey of the Jigsaw's Target Pool
     */
    @NotNull
    NamespacedKey getTargetPool();

    /**
     * Sets the Target Pool Structure key
     *
     * @param targetPool the key of target Pool
     */
    void setTargetPool(@NotNull NamespacedKey targetPool);

    /**
     * Gets the Name of the Jigsaw Block
     *
     * @return The NamespacedKey of the Jigsaw Block
     */
    @NotNull
    NamespacedKey getName();

    /**
     * Sets the Name of the Jigsaw Block
     *
     * @param name the name of the Jigsaw Block
     */
    void setName(@NotNull NamespacedKey name);

    /**
     * Gets the Target Name of the Jigsaw Block
     *
     * @return The NamespacedKey of the Jigsaw's Target Name
     */
    @NotNull
    NamespacedKey getTargetName();

    /**
     * Sets the Target Name of the Jigsaw Block
     *
     * @param targetName the target name of the Jigsaw Block
     */
    void setTargetName(@NotNull NamespacedKey targetName);

}
