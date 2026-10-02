package org.bukkit.entity;

/// Represents a MagmaCube.
public interface MagmaCube extends AbstractCubeMob, Enemy {

    /// Setting the size of the magma cube (regardless of previous size)
    /// will set the following attributes:
    ///
    ///   - [org.bukkit.attribute.Attribute#MAX_HEALTH]
    ///   - [org.bukkit.attribute.Attribute#MOVEMENT_SPEED]
    ///   - [org.bukkit.attribute.Attribute#ATTACK_DAMAGE]
    ///   - [org.bukkit.attribute.Attribute#ARMOR]
    ///
    /// to their per-size defaults and heal the
    /// magma cube to its max health (assuming it's alive).
    ///
    /// @param size the new size of the magma cube.
    void setSize(int size);
}
