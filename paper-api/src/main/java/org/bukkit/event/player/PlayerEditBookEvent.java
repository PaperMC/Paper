package org.bukkit.event.player;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.Range;

/**
 * Called when a player edits or signs a book and quill item. If the event is
 * cancelled, no changes are made to the book.
 */
public interface PlayerEditBookEvent extends PlayerEvent, Cancellable {

    /**
     * Gets the book meta currently on the book.
     * <p>
     * Note: this is a copy of the book meta. You cannot use this object to
     * change the existing book meta.
     *
     * @return the book meta currently on the book
     */
    BookMeta getPreviousBookMeta();

    /**
     * Gets the book meta that the player is attempting to add to the book.
     * <p>
     * Note: this is a copy of the proposed new book meta. Use {@link #setNewBookMeta(BookMeta)}
     * to change what will actually be added to the
     * book.
     *
     * @return the book meta that the player is attempting to add
     */
    BookMeta getNewBookMeta();

    /**
     * Sets the book meta that will actually be added to the book.
     *
     * @param newBookMeta new book meta
     */
    void setNewBookMeta(BookMeta newBookMeta);

    /**
     * Gets the inventory slot number for the book item that triggered this
     * event.
     * <p>
     * This is a slot number on the player's hotbar in the range 0-8, or -1 for
     * off hand.
     *
     * @return the inventory slot number that the book item occupies
     * @deprecated books may be signed from off hand
     */
    @Deprecated(since = "1.13.1", forRemoval = true)
    @Range(from = -1, to = 8) int getSlot();

    /**
     * Gets whether the book is being signed. If a book is signed the
     * item changes from writable_book to written_book.
     *
     * @return {@code true} if the book is being signed
     */
    boolean isSigning();

    /**
     * Sets whether the book is being signed. If a book is signed the
     * item changes from writable_book to written_book.
     *
     * @param signing whether the book is being signed.
     */
    void setSigning(boolean signing);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
