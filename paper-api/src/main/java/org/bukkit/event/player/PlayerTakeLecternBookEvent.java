package org.bukkit.event.player;

import org.bukkit.block.Lectern;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

/**
 * This event is called when a player clicks the button to take a book of a
 * Lectern. If this event is cancelled the book remains on the lectern.
 */
public interface PlayerTakeLecternBookEvent extends PlayerEventNew, Cancellable {

    /**
     * Gets the lectern involved.
     *
     * @return the Lectern
     */
    Lectern getLectern();

    /**
     * Gets the current ItemStack on the lectern.
     *
     * @return the ItemStack on the Lectern
     */
    @Nullable ItemStack getBook();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
