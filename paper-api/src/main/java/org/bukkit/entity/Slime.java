package org.bukkit.entity;

/// Represents a Slime.
public interface Slime extends AbstractCubeMob, Enemy {

    /// Setting the size of the slime (regardless of previous size)
    /// will set the following attributes:
    ///
    ///   - [org.bukkit.attribute.Attribute#MAX_HEALTH]
    ///   - [org.bukkit.attribute.Attribute#MOVEMENT_SPEED]
    ///   - [org.bukkit.attribute.Attribute#ATTACK_DAMAGE]
    ///
    /// to their per-size defaults and heal the
    /// slime to its max health (assuming it's alive).
    ///
    /// @param size the new size of the slime.
    void setSize(int size);
}
