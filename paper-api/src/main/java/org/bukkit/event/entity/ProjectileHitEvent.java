package org.bukkit.event.entity;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a projectile hits an object
 */
public class ProjectileHitEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Entity hitEntity;
    private final Block hitBlock;
    private final BlockFace hitFace;
    private boolean cancel = false;

    public ProjectileHitEvent(@NotNull final Projectile projectile) {
        this(projectile, null, null);
    }

    public ProjectileHitEvent(@NotNull final Projectile projectile, @Nullable Entity hitEntity) {
        this(projectile, hitEntity, null);
    }

    public ProjectileHitEvent(@NotNull final Projectile projectile, @Nullable Block hitBlock) {
        this(projectile, null, hitBlock);
    }

    public ProjectileHitEvent(@NotNull final Projectile projectile, @Nullable Entity hitEntity, @Nullable Block hitBlock) {
        this(projectile, hitEntity, hitBlock, null);
    }

    public ProjectileHitEvent(@NotNull final Projectile projectile, @Nullable Entity hitEntity, @Nullable Block hitBlock, @Nullable BlockFace hitFace) {
        super(projectile);
        this.hitEntity = hitEntity;
        this.hitBlock = hitBlock;
        this.hitFace = hitFace;
    }

    @NotNull
    @Override
    public Projectile getEntity() {
        return (Projectile) entity;
    }

    /**
     * Gets the block that was hit, if it was a block that was hit.
     *
     * @return hit block or else null
     */
    @Nullable
    public Block getHitBlock() {
        return hitBlock;
    }

    /**
     * Gets the block face that was hit, if it was a block that was hit and the
     * face was provided in the vent.
     *
     * @return hit face or else null
     */
    @Nullable
    public BlockFace getHitBlockFace() {
        return hitFace;
    }

    /**
     * Gets the entity that was hit, if it was an entity that was hit.
     *
     * @return hit entity or else null
     */
    @Nullable
    public Entity getHitEntity() {
        return hitEntity;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * Whether to cancel the action that occurs when the projectile hits.
     *
     * In the case of an entity, it will not collide (unless it's a firework,
     * then use {@link FireworkExplodeEvent}).
     * <br>
     * In the case of a block, some blocks (eg target block, bell) will not
     * perform the action associated.
     * <br>
     * This does NOT prevent block collisions, and explosions will still occur
     * unless their respective events are cancelled.
     *
     * @param cancel true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
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
}
