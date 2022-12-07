package org.bukkit.block.data.type;

import java.util.Set;
import org.bukkit.block.data.Directional;
import org.jetbrains.annotations.NotNull;

/**
 * Interface to the 'slot_0_occupied', 'slow_1_occupied' ... 'slot_5_occupied'
 * flags on a bookshelf which indicate which slots are occupied rendered on the
 * outside.
 * <br>
 * Block may have 0, 1... {@link #getMaximumOccupiedSlots()}-1 occupied slots.
 */
public interface ChiseledBookshelf extends Directional {

    /**
     * Checks if the following slot is occupied.
     *
     * @param slot to check
     * @return if slot is occupied
     */
    boolean isSlotOccupied(int slot);

    /**
     * Sets whether the following slot is occupied.
     *
     * @param slot to set
     * @param occupied book
     */
    void setSlotOccupied(int slot, boolean occupied);

    /**
     * Get the indexes of all the occupied slots present on this block.
     *
     * @return set of all occupied slots
     */
    @NotNull
    Set<Integer> getOccupiedSlots();

    /**
     * Get the maximum amount of slots on this block.
     *
     * @return maximum occupied slots count
     */
    int getMaximumOccupiedSlots();
}
