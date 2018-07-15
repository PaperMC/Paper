package org.bukkit.block.data;

/**
 * 'level' represents the amount of fluid contained within this block, either by
 * itself or inside a cauldron.
 * <br>
 * May not be higher than {@link #getMaximumLevel()}.
 */
public interface Levelled extends BlockData {

    /**
     * Gets the value of the 'level' property.
     *
     * @return the 'level' value
     */
    int getLevel();

    /**
     * Sets the value of the 'level' property.
     *
     * @param level the new 'level' value
     */
    void setLevel(int level);

    /**
     * Gets the maximum allowed value of the 'level' property.
     *
     * @return the maximum 'level' value
     */
    int getMaximumLevel();
}
