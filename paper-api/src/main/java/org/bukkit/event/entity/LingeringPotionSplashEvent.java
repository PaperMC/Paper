package org.bukkit.event.entity;

import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a splash potion hits an area
 */
public class LingeringPotionSplashEvent extends ProjectileHitEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final AreaEffectCloud entity;

    public LingeringPotionSplashEvent(@NotNull final ThrownPotion potion, @NotNull final AreaEffectCloud entity) {
        super(potion);
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
