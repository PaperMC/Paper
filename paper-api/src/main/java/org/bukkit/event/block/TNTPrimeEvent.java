package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a block of TNT in the world become primed.
 * <p>
 * If a TNT Prime event is cancelled, the block of TNT will not become primed.
 */
public class TNTPrimeEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final PrimeCause igniteCause;
    private final Entity primingEntity;
    private final Block primingBlock;

    public TNTPrimeEvent(@NotNull final Block block, @NotNull final PrimeCause igniteCause, @Nullable final Entity primingEntity, @Nullable final Block primingBlock) {
        super(block);
        this.igniteCause = igniteCause;
        this.primingEntity = primingEntity;
        this.primingBlock = primingBlock;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    /**
     * Get the cause of the TNT becoming primed.
     *
     * @return the cause
     */
    @NotNull
    public PrimeCause getCause() {
        return igniteCause;
    }

    /**
     * Get the entity that caused the TNT to be primed.
     *
     * @return the entity that caused the TNT to be primed, or null if it was
     * not caused by an entity.
     */
    @Nullable
    public Entity getPrimingEntity() {
        return primingEntity;
    }

    /**
     * Get the block that caused the TNT to be primed.
     *
     * @return the block that caused the TNT to be primed, or null if it was not
     * caused by a block.
     */
    @Nullable
    public Block getPrimingBlock() {
        return primingBlock;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * An enum to represent the cause of a TNT block becoming primed.
     */
    public enum PrimeCause {

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
         * When TNT with the unstable block state set to true is broken.
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
        DISPENSER;
    }
}
