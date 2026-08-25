package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.EquipmentSlot;

/**
 * Called immediately prior to a creature being leashed by a player.
 */
public interface PlayerLeashEntityEvent extends PlayerEvent, Cancellable {

    /**
     * Returns the entity that is holding the leash.
     *
     * @return The leash holder
     */
    Entity getLeashHolder();

    /**
     * Returns the entity being leashed.
     *
     * @return The entity
     */
    Entity getEntity();

    /**
     * Returns the hand used by the player to leash the entity.
     *
     * @return the hand
     */
    EquipmentSlot getHand();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
