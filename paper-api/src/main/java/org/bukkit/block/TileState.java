package org.bukkit.block;

import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a block state that also hosts a tile entity at the given location.
 *
 * This interface alone is merely a marker that does not provide any data.
 *
 * Data about the tile entities is provided by the respective interface for each
 * tile entity type.
 *
 * After modifying the data provided by a TileState, {@link #update()} needs to
 * be called to store the data.
 */
public interface TileState extends BlockState, PersistentDataHolder {

    /**
     * Returns a custom tag container capable of storing tags on the object.
     *
     * Note that the tags stored on this container are all stored under their
     * own custom namespace therefore modifying default tags using this
     * {@link PersistentDataHolder} is impossible.
     * <p>
     * This {@link PersistentDataHolder} is only linked to the snapshot instance
     * stored by the {@link BlockState}.
     *
     * When storing changes on the {@link PersistentDataHolder}, the updated
     * content will only be applied to the actual tile entity after one of the
     * {@link #update()} methods is called.
     *
     * @return the custom tag container
     */
    @NotNull
    @Override
    PersistentDataContainer getPersistentDataContainer();
}
