package org.bukkit.block;

/**
 * Represents a captured state of an ender chest.
 */
public interface EnderChest extends Lidded, TileState {
    // Paper start - More Chest Block API
    /**
     * Checks whether this ender chest is blocked by a block above
     *
     * @return whether this ender chest is blocked
     */
    boolean isBlocked();
    // Paper end - More Chest Block API
}
