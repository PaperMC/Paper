package org.bukkit.projectiles;

import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

/**
 * @since 1.7.10
 */
public interface BlockProjectileSource extends ProjectileSource {

    /**
     * Gets the block this projectile source belongs to.
     *
     * @return Block for the projectile source
     */
    @NotNull
    public Block getBlock();
}
