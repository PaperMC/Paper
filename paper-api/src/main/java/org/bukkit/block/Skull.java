package org.bukkit.block;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a captured state of a skull block.
 */
public interface Skull extends BlockState {

    /**
     * Checks to see if the skull has an owner
     *
     * @return true if the skull has an owner
     */
    public boolean hasOwner();

    /**
     * Gets the owner of the skull, if one exists
     *
     * @return the owner of the skull or null if the skull does not have an owner
     * @deprecated See {@link #getOwningPlayer()}.
     */
    @Deprecated
    @Nullable
    public String getOwner();

    /**
     * Sets the owner of the skull
     * <p>
     * Involves a potentially blocking web request to acquire the profile data for
     * the provided name.
     *
     * @param name the new owner of the skull
     * @return true if the owner was successfully set
     * @deprecated see {@link #setOwningPlayer(org.bukkit.OfflinePlayer)}.
     */
    @Deprecated
    @Contract("null -> false")
    public boolean setOwner(@Nullable String name);

    /**
     * Get the player which owns the skull. This player may appear as the
     * texture depending on skull type.
     *
     * @return owning player
     */
    @Nullable
    public OfflinePlayer getOwningPlayer();

    /**
     * Set the player which owns the skull. This player may appear as the
     * texture depending on skull type.
     *
     * @param player the owning player
     */
    public void setOwningPlayer(@NotNull OfflinePlayer player);

    /**
     * Gets the rotation of the skull in the world (or facing direction if this
     * is a wall mounted skull).
     *
     * @return the rotation of the skull
     * @deprecated use {@link BlockData}
     */
    @Deprecated
    @NotNull
    public BlockFace getRotation();

    /**
     * Sets the rotation of the skull in the world (or facing direction if this
     * is a wall mounted skull).
     *
     * @param rotation the rotation of the skull
     * @deprecated use {@link BlockData}
     */
    @Deprecated
    public void setRotation(@NotNull BlockFace rotation);

    /**
     * Gets the type of skull
     *
     * @return the type of skull
     * @deprecated check {@link Material} instead
     */
    @Deprecated
    @NotNull
    public SkullType getSkullType();

    /**
     * Sets the type of skull
     *
     * @param skullType the type of skull
     * @deprecated check {@link Material} instead
     */
    @Deprecated
    @Contract("_ -> fail")
    public void setSkullType(SkullType skullType);
}
