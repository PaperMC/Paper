package com.destroystokyo.paper.event.entity;

import java.util.Collection;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Fired when a DragonFireball collides with a block/entity and spawns an AreaEffectCloud
 */
@NullMarked
public class EnderDragonFireballHitEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Collection<LivingEntity> targets;
    private final AreaEffectCloud areaEffectCloud;
    private boolean cancelled;

    @ApiStatus.Internal
    public EnderDragonFireballHitEvent(final DragonFireball fireball, final Collection<LivingEntity> targets, final AreaEffectCloud areaEffectCloud) {
        super(fireball);
        this.targets = targets;
        this.areaEffectCloud = areaEffectCloud;
    }

    /**
     * The fireball involved in this event
     */
    @Override
    public DragonFireball getEntity() {
        return (DragonFireball) super.getEntity();
    }

    /**
     * The living entities hit by fireball
     *
     * @return the targets
     */
    public Collection<LivingEntity> getTargets() {
        return this.targets;
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
