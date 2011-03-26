package org.bukkit.event.player;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * Thrown when a player drops an item from their inventory
 */
public class PlayerDropItemEvent extends PlayerEvent implements Cancellable {
    private final Item drop;
    private boolean cancel = false;

    public PlayerDropItemEvent(final Player player, final Item drop) {
        super(Type.PLAYER_DROP_ITEM, player);
        this.drop = drop;
    }

    /**
     * Gets the ItemDrop created by the player
     *
     * @return ItemDrop
     */
    public Item getItemDrop() {
        return drop;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * If an item drop event is cancelled, the item will not be dropped and it
     * will be added back to the players inventory.
     * This will not fire an event.
     *
     * @return true if this event is cancelled
     */
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * If an item drop event is cancelled, the item will not be dropped and it
     * will be added back to the players inventory.
     * This will not fire an event.
     *
     * @param cancel true if you wish to cancel this event
     */
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
