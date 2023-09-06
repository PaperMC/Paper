package org.bukkit.block;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a captured state of a sculk shrieker.
 */
public interface SculkShrieker extends TileState {

    /**
     * Gets the most recent warning level of this block.
     *
     * When the warning level reaches 4, the shrieker will attempt to spawn a
     * Warden.
     *
     * @return current warning level
     */
    int getWarningLevel();

    /**
     * Sets the most recent warning level of this block.
     *
     * When the warning level reaches 4, the shrieker will attempt to spawn a
     * Warden.
     *
     * @param level new warning level
     */
    void setWarningLevel(int level);

    /**
     * Simulates a player causing a vibration.
     *
     * @param player the player that "caused" the shriek
     */
    void tryShriek(@Nullable Player player);
}
