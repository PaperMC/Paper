package org.bukkit.block;

import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;

import java.util.UUID;

/**
 * Represents a Skull
 */
public interface Skull extends BlockState {

    /**
     * Checks to see if the skull has an owner
     *
     * @return true if the skull has an owner
     */
    public boolean hasOwner();

    /**
     * Gets the owner of the skull
     *
     * @return the owner of the skull or null if the profile does not have a name
     * @deprecated Skulls no longer store player names, they store profiles
     * @see #getPlayer()
     */
    @Deprecated
    public String getOwner();

    /**
     * Does nothing
     *
     * @param name the new owner of the skull
     * @return true if the owner was successfully set
     * @deprecated Skulls no longer store player names, they store profiles
     * @see #setPlayer(org.bukkit.OfflinePlayer)
     */
    @Deprecated
    public boolean setOwner(String name);

    /**
     * Gets the owner of the skull, if one exists
     *
     * @return the owner of the skull or null if this skull does not have an owner
     */
    public OfflinePlayer getPlayer();

    /**
     * Sets the owner of the skull to this player
     * <p>
     * If the owner does not contain all the needed data for the skull a call to
     * {@link #update()} may potentially involve a blocking web request to acquire
     * the missing data.
     *
     * @param player the new owner of the skull
     * @return true if the owner was successfully set
     */
    public boolean setPlayer(OfflinePlayer player);

    /**
     * Gets the rotation of the skull in the world
     *
     * @return the rotation of the skull
     */
    public BlockFace getRotation();

    /**
     * Sets the rotation of the skull in the world
     *
     * @param rotation the rotation of the skull
     */
    public void setRotation(BlockFace rotation);

    /**
     * Gets the type of skull
     *
     * @return the type of skull
     */
    public SkullType getSkullType();

    /**
     * Sets the type of skull
     *
     * @param skullType the type of skull
     */
    public void setSkullType(SkullType skullType);
}
