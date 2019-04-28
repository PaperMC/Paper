package org.bukkit.inventory.meta;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a skull that can have an owner.
 */
public interface SkullMeta extends ItemMeta {

    /**
     * Gets the owner of the skull.
     *
     * @return the owner if the skull
     * @deprecated see {@link #setOwningPlayer(org.bukkit.OfflinePlayer)}.
     */
    @Deprecated
    @Nullable
    String getOwner();

    /**
     * Checks to see if the skull has an owner.
     *
     * @return true if the skull has an owner
     */
    boolean hasOwner();

    /**
     * Sets the owner of the skull.
     *
     * @param owner the new owner of the skull
     * @return true if the owner was successfully set
     * @deprecated see {@link #setOwningPlayer(org.bukkit.OfflinePlayer)}.
     */
    @Deprecated
    boolean setOwner(@Nullable String owner);

    /**
     * Gets the owner of the skull.
     *
     * @return the owner if the skull
     */
    @Nullable
    OfflinePlayer getOwningPlayer();

    /**
     * Sets the owner of the skull.
     * <p>
     * Plugins should check that hasOwner() returns true before calling this
     * plugin.
     *
     * @param owner the new owner of the skull
     * @return true if the owner was successfully set
     */
    boolean setOwningPlayer(@Nullable OfflinePlayer owner);

    @Override
    @NotNull
    SkullMeta clone();
}
