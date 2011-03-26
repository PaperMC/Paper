
package org.bukkit.event.player;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * Thrown when a player picks an item up from the ground
 */
public class PlayerPickupItemEvent extends PlayerEvent implements Cancellable {
    private final Item item;
    private boolean cancel = false;

    public PlayerPickupItemEvent(final Player player, final Item item) {
        super(Type.PLAYER_PICKUP_ITEM, player);
        this.item = item;
    }

    /**
     * Gets the ItemDrop created by the player
     *
     * @return Item
     */
    public Item getItem() {
        return item;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * If an item pickup event is cancelled, the item will not be picked up.
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
     * If an item pickup event is cancelled, the item will not be picked up.
     * This will not fire an event.
     *
     * @param cancel true if you wish to cancel this event
     */
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
