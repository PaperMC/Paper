package org.bukkit.persistence;

import org.jetbrains.annotations.NotNull;

/// The [PersistentDataHolder] interface defines an object that can store
/// custom persistent meta data on it.
///
/// Prefer using [io.papermc.paper.persistence.PersistentDataViewHolder] for read-only operations
/// as it covers more types.
public interface PersistentDataHolder extends io.papermc.paper.persistence.PersistentDataViewHolder { // Paper

    /// Returns a custom tag container capable of storing tags on the object.
    ///
    /// Note that the tags stored on this container are all stored under their
    /// own custom namespace therefore modifying default tags using this
    /// [PersistentDataHolder] is impossible.
    ///
    /// @return the persistent metadata container
    @NotNull
    PersistentDataContainer getPersistentDataContainer();

}
