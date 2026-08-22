package com.destroystokyo.paper.event.entity;

import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.EnderDragon;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEventNew;

/**
 * Fired when an EnderDragon spawns an AreaEffectCloud by shooting flames
 */
public interface EnderDragonFlameEvent extends EntityEventNew, Cancellable {

    /**
     * The enderdragon involved in this event
     */
    @Override
    EnderDragon getEntity();

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
