package io.papermc.paper.event.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Called when a player creates a filled map by right-clicking an empty map.
 */
public interface PlayerMapFilledEvent extends PlayerEvent {

    /**
     * Returns a copy of the empty map before it was consumed.
     *
     * @return cloned original item
     */
    ItemStack getOriginalItem();

    /**
     * Returns a copy of the filled map which was created.
     *
     * @return cloned created map item
     */
    ItemStack getCreatedMap();

    /**
     * Sets the filled map that will be created.
     *
     * @param createdMap map item
     */
    void setCreatedMap(ItemStack createdMap);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
