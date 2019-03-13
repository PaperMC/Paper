package org.bukkit.block;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a block (usually a container) that may be locked. When a lock is
 * active an item with a name corresponding to the key will be required to open
 * this block.
 */
public interface Lockable {

    /**
     * Checks if the container has a valid (non empty) key.
     *
     * @return true if the key is valid.
     */
    boolean isLocked();

    /**
     * Gets the key needed to access the container.
     *
     * @return the key needed.
     */
    @NotNull
    String getLock();

    /**
     * Sets the key required to access this container. Set to null (or empty
     * string) to remove key.
     *
     * @param key the key required to access the container.
     */
    void setLock(@Nullable String key);
}
