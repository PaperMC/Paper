package org.bukkit.craftbukkit.event.entity;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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
