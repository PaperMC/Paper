package org.bukkit.block.data.type;

import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

/**
 * 'mode' represents the different modes in which this structure block may
 * operate.
 */
public interface StructureBlock extends BlockData {

    /**
     * Gets the value of the 'mode' property.
     *
     * @return the 'mode' value
     */
    @NotNull
    Mode getMode();

    /**
     * Sets the value of the 'mode' property.
     *
     * @param mode the new 'mode' value
     */
    void setMode(@NotNull Mode mode);

    /**
     * Operating mode of a structure block.
     */
    public enum Mode {
        /**
         * Allows selection and saving of a structure.
         */
        SAVE,
        /**
         * Allows loading of a structure.
         */
        LOAD,
        /**
         * Used for detection of two opposite corners of a structure.
         */
        CORNER,
        /**
         * Dummy block used to run a custom function during world generation
         * before being removed.
         */
        DATA;
    }
}
