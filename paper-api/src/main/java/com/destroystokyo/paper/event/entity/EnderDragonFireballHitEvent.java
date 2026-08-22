package com.destroystokyo.paper.event.entity;

import java.util.Collection;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

/**
 * Fired when a DragonFireball collides with a block/entity and spawns an AreaEffectCloud
 */
public interface EnderDragonFireballHitEvent extends EntityEvent, Cancellable {

    /**
     * The fireball involved in this event
     */
    @Override
    DragonFireball getEntity();

    /**
     * The living entities hit by fireball
     *
     * @return the targets
     */
    Collection<LivingEntity> getTargets();

    /**
     * @return The area effect cloud spawned in this collision
     */
    AreaEffectCloud getAreaEffectCloud();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
