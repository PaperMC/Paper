package org.bukkit.event.entity;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.HandlerList;

/**
 * Called when a projectile hits an object
 */
public class ProjectileHitEvent extends EntityEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Entity hitEntity;
    private final Block hitBlock;
    private final BlockFace hitFace;

    public ProjectileHitEvent(final Projectile projectile) {
        this(projectile, null, null);
    }

    public ProjectileHitEvent(final Projectile projectile, Entity hitEntity) {
        this(projectile, hitEntity, null);
    }

    public ProjectileHitEvent(final Projectile projectile, Block hitBlock) {
        this(projectile, null, hitBlock);
    }

    public ProjectileHitEvent(final Projectile projectile, Entity hitEntity, Block hitBlock) {
        this(projectile, hitEntity, hitBlock, null);
    }

    public ProjectileHitEvent(final Projectile projectile, Entity hitEntity, Block hitBlock, BlockFace hitFace) {
        super(projectile);
        this.hitEntity = hitEntity;
        this.hitBlock = hitBlock;
        this.hitFace = hitFace;
    }

    @Override
    public Projectile getEntity() {
        return (Projectile) entity;
    }

    /**
     * Gets the block that was hit, if it was a block that was hit.
     *
     * @return hit block or else null
     */
    public Block getHitBlock() {
        return hitBlock;
    }

    /**
     * Gets the block face that was hit, if it was a block that was hit and the
     * face was provided in the vent.
     *
     * @return hit face or else null
     */
    public BlockFace getHitBlockFace() {
        return hitFace;
    }

    /**
     * Gets the entity that was hit, if it was an entity that was hit.
     *
     * @return hit entity or else null
     */
    public Entity getHitEntity() {
        return hitEntity;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
