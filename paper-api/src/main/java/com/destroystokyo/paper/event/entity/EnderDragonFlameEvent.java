package com.destroystokyo.paper.event.entity;

import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.EnderDragon;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Fired when an EnderDragon spawns an AreaEffectCloud by shooting flames
 */
@NullMarked
public class EnderDragonFlameEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final AreaEffectCloud areaEffectCloud;
    private boolean cancelled;

    @ApiStatus.Internal
    public EnderDragonFlameEvent(final EnderDragon enderDragon, final AreaEffectCloud areaEffectCloud) {
        super(enderDragon);
        this.areaEffectCloud = areaEffectCloud;
    }

    /**
     * The enderdragon involved in this event
     */
    @Override
    public EnderDragon getEntity() {
        return (EnderDragon) super.getEntity();
    }

    /**
     * @return The area effect cloud spawned in this collision
     */
    public AreaEffectCloud getAreaEffectCloud() {
        return this.areaEffectCloud;
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
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
