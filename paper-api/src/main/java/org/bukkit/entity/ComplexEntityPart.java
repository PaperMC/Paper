package org.bukkit.entity;

/**
 * Represents a single part of a {@link ComplexLivingEntity}
 */
public interface ComplexEntityPart extends Entity {
    /**
     * Gets the parent {@link ComplexLivingEntity} of this part.
     *
     * @return Parent complex entity
     */
    public ComplexLivingEntity getParent();
}
