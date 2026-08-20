package org.bukkit.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.ApiStatus;

/**
 * Represents an event that is called when a player right clicks an entity.
 *
 * @apiNote this event is no longer called without being a {@link PlayerInteractAtEntityEvent}, it's therefore
 * recommended to listen to that event instead which hold more informations.
 */
@ApiStatus.Obsolete
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
