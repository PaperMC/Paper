package org.bukkit.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a phantom.
 */
public interface Phantom extends Flying, Enemy {

    /**
     * @return The size of the phantom
     */
    public int getSize();

    /**
     * @param sz The new size of the phantom.
     */
    public void setSize(int sz);

    // Paper start
    /**
     * Get the UUID of the entity that caused this phantom to spawn
     *
     * @return UUID
     */
    @Nullable
    public java.util.UUID getSpawningEntity();
    // Paper end
}
