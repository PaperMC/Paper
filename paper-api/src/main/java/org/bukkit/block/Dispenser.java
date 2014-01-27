package org.bukkit.block;

import org.bukkit.projectiles.BlockProjectileSource;

/**
 * Represents a dispenser.
 */
public interface Dispenser extends BlockState, ContainerBlock {

    /**
     * Gets the BlockProjectileSource object for this dispenser.
     * <p>
     * If the block is no longer a dispenser, this will return null.
     *
     * @return a BlockProjectileSource if valid, otherwise null
     */
    public BlockProjectileSource getBlockProjectileSource();

    /**
     * Attempts to dispense the contents of this block.
     * <p>
     * If the block is no longer a dispenser, this will return false.
     *
     * @return true if successful, otherwise false
     */
    public boolean dispense();
}
