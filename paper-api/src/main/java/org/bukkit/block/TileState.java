package org.bukkit.block;

import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a block state that also hosts a block entity at the given location.
 * <br>
 * This interface alone is merely a marker that does not provide any data.
 * <p>
 * Data about the block entities is provided by the respective interface for each
 * block entity type.
 * <p>
 * After modifying the data provided by a TileState, {@link #update()} needs to
 * be called to store the data.
 */
public interface TileState extends BlockState, PersistentDataHolder {

    /**
     * Returns a custom tag container capable of storing tags on the object.
     * <p>
     * Note that the tags stored on this container are all stored under their
     * own custom namespace therefore modifying default tags using this
     * {@link PersistentDataHolder} is impossible.
     * <p>
     * This {@link PersistentDataHolder} is only linked to the snapshot instance
     * stored by the {@link BlockState}.
     * <p>
     * When storing changes on the {@link PersistentDataHolder}, the updated
     * content will only be applied to the actual block entity after one of the
     * {@link #update()} methods is called.
     *
     * @return the custom tag container
     */
    @NotNull
    @Override
    PersistentDataContainer getPersistentDataContainer();

    // Paper start
    /**
     * Checks if this TileState is a snapshot or a live
     * representation of the underlying block entity.
     * <p>
     * NOTE: You may still have to call {@link BlockState#update()} on
     * live representations to update any visuals on the block.
     *
     * @return true if this is a snapshot
     * @see Block#getState(boolean)
     */
    boolean isSnapshot();
    // Paper end
}
