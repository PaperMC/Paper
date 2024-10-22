package org.bukkit.block.data.type;

import org.bukkit.MinecraftExperimental;
import org.bukkit.block.data.Orientable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * 'creaking' is the creaking status of this block.
 */
@ApiStatus.Experimental
@MinecraftExperimental(MinecraftExperimental.Requires.WINTER_DROP)
public interface CreakingHeart extends Orientable {

    /**
     * Gets the value of the 'creaking' property.
     *
     * @return the 'creaking' value
     */
    @NotNull
    Creaking getCreaking();

    /**
     * Sets the value of the 'creaking' property.
     *
     * @param creaking the new 'creaking' value
     */
    void setCreaking(@NotNull Creaking creaking);

    /**
     * Creaking status.
     */
    public enum Creaking {

        /**
         * The block is disabled.
         */
        DISABLED,
        /**
         * The block is dormant.
         */
        DORMANT,
        /**
         * The block is active.
         */
        ACTIVE;
    }
}
