package org.bukkit.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Tameable extends Animals {

    /**
     * Check if this is tamed
     * <p>
     * If something is tamed then a player can not tame it through normal
     * methods, even if it does not belong to anyone in particular.
     *
     * @return true if this has been tamed
     */
    public boolean isTamed();

    /**
     * Sets if this has been tamed. Not necessary if the method setOwner has
     * been used, as it tames automatically.
     * <p>
     * If something is tamed then a player can not tame it through normal
     * methods, even if it does not belong to anyone in particular.
     *
     * @param tame true if tame
     */
    public void setTamed(boolean tame);

    // Paper start
    /**
     * Gets the owners UUID
     *
     * @return the owners UUID, or null if not owned
     */
    @Nullable
    public java.util.UUID getOwnerUniqueId();
    // Paper end

    /**
     * Gets the current owning AnimalTamer
     *
     * @see #getOwnerUniqueId() Recommended to use UUID version instead of this for performance.
     * This method will cause OfflinePlayer to be loaded from disk if the owner is not online.
     *
     * @return the owning AnimalTamer, or null if not owned
     */
    @Nullable
    public AnimalTamer getOwner();

    /**
     * Set this to be owned by given AnimalTamer.
     * <p>
     * If the owner is not null, this will be tamed and will have any current
     * path it is following removed. If the owner is set to null, this will be
     * untamed, and the current owner removed.
     *
     * @param tamer the AnimalTamer who should own this
     */
    public void setOwner(@Nullable AnimalTamer tamer);

}
