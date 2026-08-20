package io.papermc.paper.event.player;

import org.bukkit.block.Block;
import org.bukkit.block.Lectern;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEventNew;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

/**
 * This event is called when a player clicks on a lectern to insert a book.
 * If this event is cancelled the player will keep the book and the lectern will remain empty.
 */
@NullMarked
public interface PlayerInsertLecternBookEvent extends PlayerEventNew, Cancellable {

    /**
     * Gets the block of the lectern involved in this event.
     *
     * @return the block of the lectern
     */
    Block getBlock();

    /**
     * Fetches the lectern block state that was part of this event.
     * This method constructs a new snapshot {@link org.bukkit.block.BlockState}.
     *
     * @return a new lectern state snapshot of the involved lectern
     * @throws IllegalStateException if the block at {@link #getBlock()} is no longer a lectern
     */
    Lectern getLectern();

    /**
     * Returns the itemstack the player tried to insert. This is a copy of the item,
     * changes made to this itemstack will not affect the book that is being placed in the lectern.
     * If you want to mutate the item stack that ends up in the lectern, use {@link #setBook(ItemStack)}.
     *
     * @return the book that is being placed
     */
    ItemStack getBook();

    /**
     * Sets the itemstack to insert into the lectern.
     *
     * @param book the book to insert (non book items will leave the lectern in a locked
     *             state as the menu cannot be opened, preventing item extraction)
     */
    void setBook(ItemStack book);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
