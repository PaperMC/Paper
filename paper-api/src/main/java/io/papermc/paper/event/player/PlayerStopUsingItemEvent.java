package io.papermc.paper.event.player;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEventNew;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.index.qual.Positive;

/**
 * Called when the server detects a player stopping using an item.
 * Examples of this are letting go of the interact button when holding a bow, an edible item, or a spyglass.
 */
public interface PlayerStopUsingItemEvent extends PlayerEventNew {

    /**
     * Gets the exact item the player is releasing.
     *
     * @return the exact item the player released
     */
    ItemStack getItem();

    /**
     * Gets the number of ticks the item was held for.
     *
     * @return the number of ticks the item was held for
     */
    @Positive int getTicksHeldFor();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
