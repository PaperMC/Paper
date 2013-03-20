package org.bukkit.block;

import org.bukkit.SkullType;

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
     * @return the owner of the skull
     */
    public String getOwner();

    /**
     * Sets the owner of the skull
     *
     * @param name the new owner of the skull
     * @return true if the owner was successfully set
     */
    public boolean setOwner(String name);

    /**
     * Gets the rotation of the skull
     *
     * @return the rotation of the skull
     */
    public BlockFace getRotation();

    /**
     * Sets the rotation of the skull
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
