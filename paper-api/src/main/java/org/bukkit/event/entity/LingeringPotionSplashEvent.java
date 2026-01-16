package org.bukkit.event.entity;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a splash potion hits an area
 */
public class LingeringPotionSplashEvent extends ProjectileHitEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final AreaEffectCloud effectCloud;
    private boolean allowEmptyAreaEffectCreation;

    private boolean cancelled;

    @ApiStatus.Internal
    @Deprecated(since = "1.20.2", forRemoval = true)
    public LingeringPotionSplashEvent(@NotNull final ThrownPotion potion, @NotNull final AreaEffectCloud effectCloud) {
       this(potion, null, null, null, effectCloud);
    }

    @ApiStatus.Internal
    public LingeringPotionSplashEvent(@NotNull final ThrownPotion potion, @Nullable Entity hitEntity, @Nullable Block hitBlock, @Nullable BlockFace hitFace, @NotNull final AreaEffectCloud effectCloud) {
        super(potion, hitEntity, hitBlock, hitFace);
        this.effectCloud = effectCloud;
    }

    @NotNull
    @Override
    public ThrownPotion getEntity() {
        return (ThrownPotion) this.entity;
    }

    /**
     * Gets the AreaEffectCloud spawned
     *
     * @return The spawned AreaEffectCloud
     */
    @NotNull
    public AreaEffectCloud getAreaEffectCloud() {
        return effectCloud;
    }

    /**
     * Sets if an Empty AreaEffectCloud may be created
     *
     * @param allowEmptyAreaEffectCreation If an Empty AreaEffectCloud may be created
     */
    public void allowsEmptyCreation(boolean allowEmptyAreaEffectCreation) {
        this.allowEmptyAreaEffectCreation = allowEmptyAreaEffectCreation;
    }

    /**
     * Gets if an empty AreaEffectCloud may be created
     *
     * @return if an empty AreaEffectCloud may be created
     */
    public boolean allowsEmptyCreation() {
        return this.allowEmptyAreaEffectCreation;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

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
