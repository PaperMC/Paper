package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;

/**
 * Represents a player related inventory event
 */
@SuppressWarnings("serial")
public class PlayerInventoryEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    protected Inventory inventory;

    public PlayerInventoryEvent(final Player player, final Inventory inventory) {
        super(Type.PLAYER_INVENTORY, player);
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

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
