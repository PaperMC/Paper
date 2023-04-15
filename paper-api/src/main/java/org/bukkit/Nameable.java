package org.bukkit;

import org.jetbrains.annotations.Nullable;

/**
 * Represents a block, entity, or other object that may receive a custom name.
 */
public interface Nameable {

    /**
     * Gets the custom name on a mob or block. If there is no name this method
     * will return null.
     * <p>
     * This value has no effect on players, they will always use their real
     * name.
     *
     * @return name of the mob/block or null
     */
    @Nullable
    public String getCustomName();

    /**
     * Sets a custom name on a mob or block. This name will be used in death
     * messages and can be sent to the client as a nameplate over the mob.
     * <p>
     * Setting the name to null or an empty string will clear it.
     * <p>
     * This value has no effect on players, they will always use their real
     * name.
     *
     * @param name the name to set
     */
    public void setCustomName(@Nullable String name);
}
