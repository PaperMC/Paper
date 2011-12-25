package org.bukkit.event.player;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * Thrown when a player drops an item from their inventory
 */
@SuppressWarnings("serial")
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
     * @return ItemDrop created by the player
     */
    public Item getItemDrop() {
        return drop;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
