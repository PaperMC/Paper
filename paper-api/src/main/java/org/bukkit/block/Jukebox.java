package org.bukkit.block;

import org.bukkit.Material;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.JukeboxInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a captured state of a jukebox.
 *
 * @since 1.0.0 R1
 */
public interface Jukebox extends io.papermc.paper.block.TileStateInventoryHolder { // Paper - TileStateInventoryHolder

    /**
     * Gets the record inserted into the jukebox.
     *
     * @return The record Material, or AIR if none is inserted
     */
    @NotNull
    public Material getPlaying();

    /**
     * Sets the record being played.
     *
     * @param record The record Material, or null/AIR to stop playing
     */
    public void setPlaying(@Nullable Material record);

    /**
     * Gets whether or not this jukebox has a record.
     * <p>
     * A jukebox can have a record but not {@link #isPlaying() be playing}
     * if it was stopped with {@link #stopPlaying()} or if a record has
     * finished playing.
     *
     * @return true if this jukebox has a record, false if it the jukebox
     * is empty
     * @since 1.19.4
     */
    public boolean hasRecord();

    /**
     * Gets the record item inserted into the jukebox.
     *
     * @return a copy of the inserted record, or an air stack if none
     * @since 1.13.2
     */
    @NotNull
    public ItemStack getRecord();

    /**
     * Sets the record being played. The jukebox will start playing automatically.
     *
     * @param record the record to insert or null/AIR to empty
     * @since 1.13.2
     */
    public void setRecord(@Nullable ItemStack record);

    /**
     * Checks if the jukebox is playing a record.
     *
     * @return True if there is a record playing
     */
    public boolean isPlaying();

    /**
     * Starts the jukebox playing if there is a record.
     *
     * @return true if the jukebox had a record and was able to start playing, false
     * if the jukebox was already playing or did not have a record
     * @since 1.19.4
     */
    public boolean startPlaying();

    /**
     * Stops the jukebox playing without ejecting the record.
     *
     * @since 1.16.1
     */
    public void stopPlaying();

    /**
     * Stops the jukebox playing and ejects the current record.
     * <p>
     * If the block represented by this state is no longer a jukebox, this will
     * do nothing and return false.
     *
     * @return True if a record was ejected; false if there was none playing
     * @throws IllegalStateException if this block state is not placed
     */
    public boolean eject();

    /**
     * @since 1.19.4
     */
    // Paper - move docs to TileStateInventoryHolder
    @NotNull
    @Override
    JukeboxInventory getInventory();

    /**
     * @since 1.19.4
     */
    @Override // Paper - move docs to TileStateInventoryHolder
    @NotNull
    JukeboxInventory getSnapshotInventory();
}
