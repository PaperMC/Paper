package org.bukkit.event.entity;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a projectile hits an object
 */
public class ProjectileHitEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Entity hitEntity;
    private final Block hitBlock;
    private final BlockFace hitFace;

    private boolean cancelled;

    @ApiStatus.Internal
    @Deprecated(forRemoval = true)
    public ProjectileHitEvent(@NotNull final Projectile projectile) {
        this(projectile, null, null, null);
    }

    @ApiStatus.Internal
    @Deprecated(forRemoval = true)
    public ProjectileHitEvent(@NotNull final Projectile projectile, @Nullable Entity hitEntity) {
        this(projectile, hitEntity, null, null);
    }

    @ApiStatus.Internal
    @Deprecated(forRemoval = true)
    public ProjectileHitEvent(@NotNull final Projectile projectile, @Nullable Block hitBlock) {
        this(projectile, null, hitBlock, null);
    }

    @ApiStatus.Internal
    @Deprecated(forRemoval = true)
    public ProjectileHitEvent(@NotNull final Projectile projectile, @Nullable Entity hitEntity, @Nullable Block hitBlock) {
        this(projectile, hitEntity, hitBlock, null);
    }

    @ApiStatus.Internal
    public ProjectileHitEvent(@NotNull final Projectile projectile, @Nullable Entity hitEntity, @Nullable Block hitBlock, @Nullable BlockFace hitFace) {
        super(projectile);
        this.hitEntity = hitEntity;
        this.hitBlock = hitBlock;
        this.hitFace = hitFace;
    }

    @NotNull
    @Override
    public Projectile getEntity() {
        return (Projectile) this.entity;
    }

    /**
     * Gets the entity that was hit, if it was an entity that was hit.
     *
     * @return hit entity or else {@code null}
     */
    @Nullable
    public Entity getHitEntity() {
        return this.hitEntity;
    }

    /**
     * Gets the block that was hit, if it was a block that was hit.
     *
     * @return hit block or else {@code null}
     */
    @Nullable
    public Block getHitBlock() {
        return this.hitBlock;
    }

    /**
     * Gets the block face that was hit, if it was a block that was hit and the
     * face was provided in the event.
     *
     * @return hit face or else {@code null}
     */
    @Nullable
    public BlockFace getHitBlockFace() {
        return this.hitFace;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Whether to cancel the action that occurs when the projectile hits.
     * <p>
     * In the case of an entity, it will not collide (unless it's a firework,
     * then use {@link FireworkExplodeEvent}).
     * <br>
     * In the case of a block, some blocks (e.g. target block, bell) will not
     * perform the action associated.
     * <p>
     * This does NOT prevent block collisions, and explosions will still occur
     * unless their respective events are cancelled.
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
