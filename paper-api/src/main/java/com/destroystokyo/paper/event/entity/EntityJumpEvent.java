package com.destroystokyo.paper.event.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEventNew;

/**
 * Called when an entity jumps
 * <p>
 * Cancelling the event will stop the entity from jumping
 */
public interface EntityJumpEvent extends EntityEventNew, Cancellable {

    @Override
    LivingEntity getEntity();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
