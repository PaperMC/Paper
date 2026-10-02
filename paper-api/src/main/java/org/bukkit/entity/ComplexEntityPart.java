package org.bukkit.entity;

import org.jetbrains.annotations.NotNull;

/// Represents a single part of a [ComplexLivingEntity]
public interface ComplexEntityPart extends Entity {

    /// Gets the parent [ComplexLivingEntity] of this part.
    ///
    /// @return Parent complex entity
    @NotNull
    public ComplexLivingEntity getParent();
}
