package org.bukkit.entity.minecart;

import com.destroystokyo.paper.loottable.LootableEntityInventory;
import org.bukkit.entity.Minecart;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.loot.Lootable;

/**
 * Represents a Minecart with a Hopper inside it
 *
 * @since 1.5.1 R0.2
 */
public interface HopperMinecart extends Minecart, InventoryHolder, LootableEntityInventory {

    /**
     * Checks whether or not this Minecart will pick up
     * items into its inventory.
     *
     * @return true if the Minecart will pick up items
     * @since 1.9.4
     */
    boolean isEnabled();

    /**
     * Sets whether this Minecart will pick up items.
     *
     * @param enabled new enabled state
     * @since 1.9.4
     */
    void setEnabled(boolean enabled);
    // Paper start
    /**
     * Gets the number of ticks that this hopper minecart cannot pickup items up for.
     *
     * @return ticks left on cooldown
     * @deprecated Hopper minecarts don't have cooldowns anymore
     * @since 1.19.2
     */
    @Deprecated(forRemoval = true, since = "1.19.4")
    int getPickupCooldown();

    /**
     * Sets the number of ticks that this hopper minecart cannot pickup items for.
     *
     * @param cooldown cooldown length in ticks
     * @deprecated Hopper minecarts don't have cooldowns anymore
     * @since 1.19.2
     */
    @Deprecated(forRemoval = true, since = "1.19.4")
    void setPickupCooldown(int cooldown);
    // Paper end
}
