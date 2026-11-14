package org.bukkit.projectiles;

import org.bukkit.block.Block;

/**
 * A {@link ProjectileSource} that is a block, such as a dispenser.
 */
public interface BlockProjectileSource extends ProjectileSource {

    /**
     * Gets the block this projectile source belongs to.
     *
     * @return Block for the projectile source
     */
    Block getBlock();
}
