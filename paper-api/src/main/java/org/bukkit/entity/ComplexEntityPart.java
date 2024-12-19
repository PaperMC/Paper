package org.bukkit.entity;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a single part of a {@link ComplexLivingEntity}
 *
 * @since 1.0.0 R1
 */
public interface ComplexEntityPart extends Entity {

    /**
     * Gets the parent {@link ComplexLivingEntity} of this part.
     *
     * @return Parent complex entity
     */
    @NotNull
    public ComplexLivingEntity getParent();
}
