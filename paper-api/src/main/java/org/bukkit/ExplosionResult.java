package org.bukkit;

import org.jetbrains.annotations.ApiStatus;

/**
 * Represents the outcome of an explosion.
 */
@ApiStatus.Experimental
public enum ExplosionResult {

    /**
     * Represents an explosion where no change took place.
     *
     * This is the case when {@link org.bukkit.GameRule#MOB_GRIEFING} is
     * disabled.
     */
    KEEP,
    /**
     * Represents an explosion where all destroyed blocks drop their items.
     *
     * This is the case when
     * {@link org.bukkit.GameRule#TNT_EXPLOSION_DROP_DECAY} or
     * {@link org.bukkit.GameRule#BLOCK_EXPLOSION_DROP_DECAY} is disabled.
     */
    DESTROY,
    /**
     * Represents an explosion where explosions cause only some blocks to drop.
     */
    DESTROY_WITH_DECAY,
    /**
     * Represents an explosion where a block change/update has happened.
     *
     * For example, when a wind charge is used it will cause nearby buttons,
     * levers and bells to be activated.
     */
    TRIGGER_BLOCK
}
