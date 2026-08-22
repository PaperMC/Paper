package com.destroystokyo.paper.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LightningStrike;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityTransformEvent;

/**
 * Fired when lightning strikes an entity
 */
public interface EntityZapEvent extends EntityTransformEvent {

    /**
     * Gets the lightning bolt that is striking the entity.
     *
     * @return The lightning bolt responsible for this event
     */
    LightningStrike getBolt();

    /**
     * Gets the entity that will replace the struck entity.
     *
     * @return The entity that will replace the struck entity
     */
    default Entity getReplacementEntity() {
        return this.getTransformedEntity();
    }

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
