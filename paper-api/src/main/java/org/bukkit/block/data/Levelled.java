package org.bukkit.block.data;

/**
 * 'level' represents the amount of fluid contained within this block, either by
 * itself or inside a cauldron.
 * <br>
 * In the case of water and lava blocks the levels have special meanings: a
 * level of 0 corresponds to a source block, 1-7 regular fluid heights, and 8-15
 * to "falling" fluids. All falling fluids have the same behaviour, but the
 * level corresponds to that of the block above them, equal to
 * <code>this.level - 8</code>
 * <b>Note that counterintuitively, an adjusted level of 1 is the highest level,
 * whilst 7 is the lowest.</b>
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

    // Paper start
    /**
     * Gets the minimum allowed value of the 'level' property.
     *
     * @return the minimum 'level' value
     */
    int getMinimumLevel();
    // Paper end
}
