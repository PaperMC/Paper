package io.papermc.paper.event.player;

import org.bukkit.block.Lectern;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

public interface PlayerLecternPageChangeEvent extends PlayerEvent, Cancellable {

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
    ItemStack getBook();

    /**
     * Gets the page change direction. This is essentially returns which button the player clicked, left or right.
     *
     * @return the page change direction
     */
    PageChangeDirection getPageChangeDirection();

    /**
     * Gets the page changed from. <i>Pages are 0-indexed.</i>
     *
     * @return the page changed from
     */
    int getOldPage();

    /**
     * Gets the page changed to. <i>Pages are 0-indexed.</i>
     *
     * @return the page changed to
     */
    int getNewPage();

    /**
     * Sets the page changed to. <i>Pages are 0-indexed.</i>
     * Page indices that are greater than the number of pages will show the last page.
     *
     * @param newPage the new paged changed to
     */
    void setNewPage(int newPage);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    enum PageChangeDirection {
        LEFT,
        RIGHT,
    }
}
