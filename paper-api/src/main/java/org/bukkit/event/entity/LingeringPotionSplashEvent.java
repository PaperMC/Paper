package org.bukkit.event.entity;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a splash potion hits an area
 */
public class LingeringPotionSplashEvent extends ProjectileHitEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final AreaEffectCloud entity;

    @Deprecated
    public LingeringPotionSplashEvent(@NotNull final ThrownPotion potion, @NotNull final AreaEffectCloud entity) {
       this(potion, null, null, null, entity);
    }

    public LingeringPotionSplashEvent(@NotNull final ThrownPotion potion, @Nullable Entity hitEntity, @Nullable Block hitBlock, @Nullable BlockFace hitFace, @NotNull final AreaEffectCloud entity) {
        super(potion, hitEntity, hitBlock, hitFace);
        this.entity = entity;
    }

    @NotNull
    @Override
    public ThrownPotion getEntity() {
        return (ThrownPotion) super.getEntity();
    }

    /**
     * Gets the AreaEffectCloud spawned
     *
     * @return The spawned AreaEffectCloud
     */
    @NotNull
    public AreaEffectCloud getAreaEffectCloud() {
        return entity;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
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
