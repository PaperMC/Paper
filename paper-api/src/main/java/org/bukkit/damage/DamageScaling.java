package org.bukkit.damage;

import org.bukkit.entity.Player;

/// A means of damage scaling with respect to the server's difficulty.
public enum DamageScaling {

    /// Damage is not scaled.
    NEVER,
    /// Damage is scaled only when the
    /// [`causing entity`][DamageSource#getCausingEntity()] is not a
    /// [Player].
    WHEN_CAUSED_BY_LIVING_NON_PLAYER,
    /// Damage is always scaled.
    ALWAYS;
}
