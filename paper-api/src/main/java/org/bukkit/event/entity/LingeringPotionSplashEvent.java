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
 *
 * @since 1.9.4
 */
public class LingeringPotionSplashEvent extends ProjectileHitEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final AreaEffectCloud entity;
    private boolean allowEmptyAreaEffectCreation; // Paper

    @Deprecated(since = "1.20.2")
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

    // Paper start
    /**
     * Sets if an Empty AreaEffectCloud may be created
     *
     * @param allowEmptyAreaEffectCreation If an Empty AreaEffectCloud may be created
     * @since 1.20.2
     */
    public void allowsEmptyCreation(boolean allowEmptyAreaEffectCreation) {
        this.allowEmptyAreaEffectCreation = allowEmptyAreaEffectCreation;
    }

    /**
     * Gets if an empty AreaEffectCloud may be created
     *
     * @return if an empty AreaEffectCloud may be created
     * @since 1.20.2
     */
    public boolean allowsEmptyCreation() {
        return allowEmptyAreaEffectCreation;
    }
    // Paper end

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
