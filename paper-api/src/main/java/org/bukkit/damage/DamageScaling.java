package org.bukkit.damage;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

/**
 * A means of damage scaling with respect to the server's difficulty.
 */
@ApiStatus.Experimental
public enum DamageScaling {

    /**
     * Damage is not scaled.
     */
    NEVER,
    /**
     * Damage is scaled only when the
     * {@link DamageSource#getCausingEntity() causing entity} is not a
     * {@link Player}.
     */
    WHEN_CAUSED_BY_LIVING_NON_PLAYER,
    /**
     * Damage is always scaled.
     */
    ALWAYS;
}
