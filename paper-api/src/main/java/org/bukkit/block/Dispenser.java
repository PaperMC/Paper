package org.bukkit.block;

import org.bukkit.Nameable;
import org.bukkit.loot.Lootable;
import org.bukkit.projectiles.BlockProjectileSource;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a captured state of a dispenser.
 */
public interface Dispenser extends Container, Nameable, Lootable {

    /**
     * Gets the BlockProjectileSource object for the dispenser.
     * <p>
     * If the block represented by this state is no longer a dispenser, this
     * will return null.
     *
     * @return a BlockProjectileSource if valid, otherwise null
     * @throws IllegalStateException if this block state is not placed
     */
    @Nullable
    public BlockProjectileSource getBlockProjectileSource();

    /**
     * Attempts to dispense the contents of the dispenser.
     * <p>
     * If the block represented by this state is no longer a dispenser, this
     * will return false.
     *
     * @return true if successful, otherwise false
     * @throws IllegalStateException if this block state is not placed
     */
    public boolean dispense();
}
