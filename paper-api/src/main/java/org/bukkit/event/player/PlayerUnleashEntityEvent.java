package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityUnleashEvent;
import org.bukkit.inventory.EquipmentSlot;

/**
 * Called prior to an entity being unleashed due to a player's action.
 */
public interface PlayerUnleashEntityEvent extends EntityUnleashEvent {

    /**
     * Returns the player who is unleashing the entity.
     *
     * @return The player
     */
    Player getPlayer();

    /**
     * Get the hand used by the player to unleash the entity.
     *
     * @return the hand
     */
    EquipmentSlot getHand();
}
