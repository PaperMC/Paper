package io.papermc.paper.event.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a player picks up a crafted item from the result slot of a crafting grid.
 */
@NullMarked
public interface ItemCraftedEvent extends Event {

    /**
     * Gets the player who triggered the event by picking up the crafted item.
     *
     * @return the player
     */
    Player getPlayer();

    /**
     * Gets the item that was crafted and picked up by the player.
     *
     * @return the crafted item
     */
    ItemStack getCraftedItem();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
