package org.bukkit.entity.minecart;

import com.destroystokyo.paper.loottable.LootableEntityInventory;
import org.bukkit.entity.Minecart;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.loot.Lootable;

/**
 * Represents a Minecart with a Hopper inside it
 */
public interface HopperMinecart extends Minecart, InventoryHolder, LootableEntityInventory {

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
    // Paper start
    /**
     * Gets the number of ticks that this hopper minecart cannot pickup items up for.
     *
     * @return ticks left on cooldown
     * @deprecated Hopper minecarts don't have cooldowns anymore
     */
    @Deprecated(forRemoval = true, since = "1.19.4")
    int getPickupCooldown();

    /**
     * Sets the number of ticks that this hopper minecart cannot pickup items for.
     *
     * @param cooldown cooldown length in ticks
     * @deprecated Hopper minecarts don't have cooldowns anymore
     */
    @Deprecated(forRemoval = true, since = "1.19.4")
    void setPickupCooldown(int cooldown);
    // Paper end
}
