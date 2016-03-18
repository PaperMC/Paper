package org.bukkit.entity.minecart;

import org.bukkit.entity.Minecart;
import org.bukkit.inventory.InventoryHolder;

/**
 * Represents a Minecart with a Hopper inside it
 */
public interface HopperMinecart extends Minecart, InventoryHolder {

    /**
     * Checks whether or not this Minecart will pick up 
     * items into its inventory.
     * 
     * @return true if the Minecart will pick up items
     */
    boolean isEnabled();

    /**
     * Sets whether this Minecart will pick up items.
     * 
     * @param enabled new enabled state
     */
    void setEnabled(boolean enabled);
}
