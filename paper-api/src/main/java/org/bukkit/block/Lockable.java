package org.bukkit.block;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
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
     * @deprecated locks are not necessarily pure strings
     */
    @NotNull
    @Deprecated(since = "1.21.2")
    String getLock();

    /**
     * Sets the key required to access this container. Set to null (or empty
     * string) to remove key.
     *
     * @param key the key required to access the container.
     * @deprecated locks are not necessarily pure strings
     */
    @Deprecated(since = "1.21.2")
    void setLock(@Nullable String key);

    /**
     * Sets the key required to access this container. All explicit
     * modifications to the set key will be required to match on the opening
     * key. Set to null to remove key.
     *
     * @param key the key required to access the container.
     */
    @ApiStatus.Experimental
    void setLockItem(@Nullable ItemStack key);
}
