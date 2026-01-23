package org.bukkit.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;

/**
 * Represents an event that is called when a player right clicks an entity.
 */
public interface PlayerInteractEntityEvent extends PlayerEventNew, Cancellable {

    /**
     * Gets the entity that was right-clicked by the player.
     *
     * @return entity right clicked by player
     */
    Entity getRightClicked();

    /**
     * The hand used to perform this interaction.
     *
     * @return the hand used to interact
     */
    EquipmentSlot getHand();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
