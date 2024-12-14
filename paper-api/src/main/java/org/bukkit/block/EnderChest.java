package org.bukkit.block;

/**
 * Represents a captured state of an ender chest.
 *
 * @since 1.11
 */
public interface EnderChest extends Lidded, TileState {
    // Paper start - More Chest Block API
    /**
     * Checks whether this ender chest is blocked by a block above
     *
     * @return whether this ender chest is blocked
     * @since 1.20.6
     */
    boolean isBlocked();
    // Paper end - More Chest Block API
}
