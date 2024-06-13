package org.bukkit.inventory;

import org.bukkit.block.Jukebox;
import org.jetbrains.annotations.Nullable;

/**
 * Interface to the inventory of a Jukebox.
 */
public interface JukeboxInventory extends Inventory {

    /**
     * Set the record in the jukebox.
     * <p>
     * This will immediately start playing the inserted item or stop playing if the
     * item provided is null.
     *
     * @param item the new record
     */
    void setRecord(@Nullable ItemStack item);

    /**
     * Get the record in the jukebox.
     *
     * @return the current record
     */
    @Nullable
    ItemStack getRecord();

    @Nullable
    @Override
    public Jukebox getHolder();
}
