package org.bukkit.craftbukkit.event.entity;

import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.jspecify.annotations.Nullable;

public class CraftProjectileHitEvent extends CraftEntityEvent implements ProjectileHitEvent {

    private final @Nullable Entity hitEntity;
    private final @Nullable Block hitBlock;
    private final @Nullable BlockFace hitFace;

    private boolean cancelled;

    public CraftProjectileHitEvent(final Projectile projectile, final @Nullable Entity hitEntity, final @Nullable Block hitBlock, final @Nullable BlockFace hitFace) {
        super(projectile);
        this.hitEntity = hitEntity;
        this.hitBlock = hitBlock;
        this.hitFace = hitFace;
    }

    public CraftProjectileHitEvent(final net.minecraft.world.entity.projectile.Projectile projectile, final @Nullable HitResult hitResult) {
        Block hitBlock = null;
        BlockFace hitFace = null;
        if (hitResult instanceof final BlockHitResult blockHitResult) {
            hitBlock = CraftBlock.at(projectile.level(), blockHitResult.getBlockPos());
            hitFace = CraftBlock.notchToBlockFace(blockHitResult.getDirection());
        }

        Entity hitEntity = null;
        if (hitResult instanceof final EntityHitResult entityHitResult) {
            hitEntity = entityHitResult.getEntity().getBukkitEntity();
        }

        this((Projectile) projectile.getBukkitEntity(), hitEntity, hitBlock, hitFace);
    }

    @Override
    public Projectile getEntity() {
        return (Projectile) this.entity;
    }

    @Override
    public @Nullable Entity getHitEntity() {
        return this.hitEntity;
    }

    @Override
    public @Nullable Block getHitBlock() {
        return this.hitBlock;
    }

    @Override
    public @Nullable BlockFace getHitBlockFace() {
        return this.hitFace;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return ProjectileHitEvent.getHandlerList();
    }
}
