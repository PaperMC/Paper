package org.bukkit.entity.minecart;

import com.destroystokyo.paper.loottable.LootableEntityInventory;
import org.bukkit.entity.Minecart;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

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

    // Paper start - Allow you to set the number of items that a hopper moves

    /**
     * Define the number of items transferred by the hopper; the amount must be strictly greater than 0.
     * Setting a value to null corresponds to resuming the server's default behavior.
     * @param transferAmount Items amount
     */
    void setTransferAmount(@org.jetbrains.annotations.Range(from = 1, to = Integer.MAX_VALUE) @org.jspecify.annotations.Nullable Integer transferAmount);

    /**
     * Retrieve the number of items transferred by the hopper. If there is no value, it refers to the configuration of
     * the server being used.
     * @return Items amount
     */
    @NotNull
    java.util.Optional<Integer> getTransferAmount();
    // Paper end - Allow you to set the number of items that a hopper moves
}
