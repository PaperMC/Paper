package org.bukkit.inventory.meta;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

/**
 * Represents a skull ({@link Material#SKULL_ITEM}) that can have an owner.
 */
public interface SkullMeta extends ItemMeta {

    /**
     * Gets the owner of the skull.
     *
     * @return the owner if the skull
     * @deprecated see {@link #setOwningPlayer(org.bukkit.OfflinePlayer)}.
     */
    @Deprecated
    String getOwner();

    /**
     * Checks to see if the skull has an owner.
     *
     * @return true if the skull has an owner
     */
    boolean hasOwner();

    /**
     * Sets the owner of the skull.
     * <p>
     * Plugins should check that hasOwner() returns true before calling this
     * plugin.
     *
     * @param owner the new owner of the skull
     * @return true if the owner was successfully set
     * @deprecated see {@link #setOwningPlayer(org.bukkit.OfflinePlayer)}.
     */
    @Deprecated
    boolean setOwner(String owner);

    /**
     * Gets the owner of the skull.
     *
     * @return the owner if the skull
     */
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
    boolean setOwningPlayer(OfflinePlayer owner);

    SkullMeta clone();
}
