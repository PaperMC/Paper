package org.bukkit.block;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a captured state of a jigsaw.
 */
public interface Jigsaw extends TileState {

    /**
     * Gets the target pool structure key.
     *
     * @return the {@link NamespacedKey} of the jigsaw's target pool.
     */
    @NotNull
    NamespacedKey getTargetPool();

    /**
     * Sets the target pool structure key.
     *
     * @param targetPool the key of target pool
     */
    void setTargetPool(@NotNull NamespacedKey targetPool);

    /**
     * Gets the name of the jigsaw block.
     *
     * @return The NamespacedKey of the jigsaw block
     */
    @NotNull
    NamespacedKey getName();

    /**
     * Sets the name of the jigsaw block.
     *
     * @param name the name of the jigsaw block
     */
    void setName(@NotNull NamespacedKey name);

    /**
     * Gets the target name of the jigsaw block.
     *
     * @return The NamespacedKey of the jigsaw's target name
     */
    @NotNull
    NamespacedKey getTargetName();

    /**
     * Sets the target name of the jigsaw block.
     *
     * @param targetName the target name of the jigsaw block
     */
    void setTargetName(@NotNull NamespacedKey targetName);

}
