package org.bukkit.block;

import com.destroystokyo.paper.loottable.LootableBlockInventory;
import org.jetbrains.annotations.NotNull;
import java.util.Optional;

/**
 * Represents a captured state of a hopper.
 */
public interface Hopper extends Container, LootableBlockInventory { // Paper
    // Paper start - Expanded Hopper API
    /**
     * Sets the cooldown before the hopper transfers or sucks in another item
     * @param cooldown the cooldown in ticks
     * @throws IllegalArgumentException if the passed cooldown value is negative.
     */
    void setTransferCooldown(@org.jetbrains.annotations.Range(from = 0, to = Integer.MAX_VALUE) int cooldown);

    /**
     * Returns the cooldown before the hopper transfers or sucks in another item
     * @return the cooldown in ticks
     */
    int getTransferCooldown();
    // Paper end - Expanded Hopper API

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
    Optional<Integer> getTransferAmount();
    // Paper end - Allow you to set the number of items that a hopper moves
}

