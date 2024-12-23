package org.bukkit.entity;

import org.jetbrains.annotations.NotNull;

/**
 * Represents an ender dragon part
 *
 * @since 1.0.0
 */
public interface EnderDragonPart extends ComplexEntityPart, Damageable {
    @Override
    @NotNull
    public EnderDragon getParent();
}
