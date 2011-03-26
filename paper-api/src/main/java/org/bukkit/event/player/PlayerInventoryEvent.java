
package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * Represents a player related inventory event
 */
public class PlayerInventoryEvent extends PlayerEvent {
    protected Inventory inventory;

    public PlayerInventoryEvent(final Player player, final Inventory inventory) {
        super(Type.PLAYER_INTERACT, player);
        this.inventory = inventory;
    }

    /**
     * Gets the Inventory involved in this event
     *
     * @return Inventory
     */
    public Inventory getInventory() {
        return inventory;
    }
}
