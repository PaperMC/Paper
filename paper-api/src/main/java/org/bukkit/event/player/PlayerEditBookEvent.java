package org.bukkit.event.player;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player edits or signs a book and quill item. If the event is
 * cancelled, no changes are made to the BookMeta
 */
public class PlayerEditBookEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final BookMeta previousBookMeta;
    private final int slot;
    private BookMeta newBookMeta;
    private boolean isSigning;

    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerEditBookEvent(@NotNull Player player, int slot, @NotNull BookMeta previousBookMeta, @NotNull BookMeta newBookMeta, boolean isSigning) {
        super(player);

        this.previousBookMeta = previousBookMeta;
        this.newBookMeta = newBookMeta;
        this.slot = slot;
        this.isSigning = isSigning;
    }

    /**
     * Gets the book meta currently on the book.
     * <p>
     * Note: this is a copy of the book meta. You cannot use this object to
     * change the existing book meta.
     *
     * @return the book meta currently on the book
     */
    @NotNull
    public BookMeta getPreviousBookMeta() {
        return this.previousBookMeta.clone();
    }

    /**
     * Gets the book meta that the player is attempting to add to the book.
     * <p>
     * Note: this is a copy of the proposed new book meta. Use {@link
     * #setNewBookMeta(BookMeta)} to change what will actually be added to the
     * book.
     *
     * @return the book meta that the player is attempting to add
     */
    @NotNull
    public BookMeta getNewBookMeta() {
        return this.newBookMeta.clone();
    }

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
    public int getSlot() {
        return this.slot;
    }

    /**
     * Sets the book meta that will actually be added to the book.
     *
     * @param newBookMeta new book meta
     * @throws IllegalArgumentException if the new book meta is null
     */
    public void setNewBookMeta(@NotNull BookMeta newBookMeta) throws IllegalArgumentException {
        Preconditions.checkArgument(newBookMeta != null, "New book meta must not be null");
        Bukkit.getItemFactory().equals(newBookMeta, null);
        this.newBookMeta = newBookMeta.clone();
    }

    /**
     * Gets whether the book is being signed. If a book is signed the
     * Material changes from BOOK_AND_QUILL to WRITTEN_BOOK.
     *
     * @return {@code true} if the book is being signed
     */
    public boolean isSigning() {
        return this.isSigning;
    }

    /**
     * Sets whether the book is being signed. If a book is signed the
     * Material changes from BOOK_AND_QUILL to WRITTEN_BOOK.
     *
     * @param signing whether the book is being signed.
     */
    public void setSigning(boolean signing) {
        this.isSigning = signing;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
