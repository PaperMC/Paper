package org.bukkit.entity;

/**
 * Represents Horse-like creatures which can carry an inventory.
 */
public interface ChestedHorse extends AbstractHorse {

    /**
     * Gets whether the horse has a chest equipped.
     *
     * @return true if the horse has chest storage
     */
    public boolean isCarryingChest();

    /**
     * Sets whether the horse has a chest equipped. Removing a chest will also
     * clear the chest's inventory.
     *
     * @param chest true if the horse should have a chest
     */
    public void setCarryingChest(boolean chest);
}
