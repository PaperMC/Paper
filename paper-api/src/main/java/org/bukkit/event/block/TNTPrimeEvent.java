package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

/**
 * Called when a block of TNT in the world become primed.
 * <p>
 * If this event is cancelled, the block of TNT will not become primed.
 */
public interface TNTPrimeEvent extends BlockEvent, Cancellable {

    /**
     * Get the cause of the TNT becoming primed.
     *
     * @return the cause
     */
    PrimeCause getCause();

    /**
     * Get the entity that caused the TNT to be primed.
     *
     * @return the entity that caused the TNT to be primed, or {@code null} if it was
     * not caused by an entity.
     */
    @Nullable Entity getPrimingEntity();

    /**
     * Get the block that caused the TNT to be primed.
     *
     * @return the block that caused the TNT to be primed, or {@code null} if it was not
     * caused by a block.
     */
    @Nullable Block getPrimingBlock();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    /**
     * An enum to represent the cause of a TNT block becoming primed.
     */
    enum PrimeCause {

        /**
         * When TNT is primed by fire spreading.
         */
        FIRE,
        /**
         * When TNT is primed by a redstone signal.
         */
        REDSTONE,
        /**
         * When TNT is primed by a player interacting with it directly.
         */
        PLAYER,
        /**
         * When TNT is primed by a nearby explosion.
         */
        EXPLOSION,
        /**
         * When TNT is primed after getting hit with a burning projectile.
         */
        PROJECTILE,
        /**
         * When TNT with the unstable block state set to {@code true} is broken.
         * <p>
         * Note: Canceling a prime event with this cause will stop the primed
         * TNT from spawning but will not stop the block from being broken.
         */
        BLOCK_BREAK,
        /**
         * When TNT is primed by a dispenser holding flint and steel.
         * <p>
         * Note: This event is not called for a dispenser dispensing TNT
         * directly.
         */
        DISPENSER
    }
}
