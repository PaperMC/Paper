package io.papermc.paper.event.player;

import com.google.common.base.Preconditions;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Lectern;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * This event is called when a player clicks on a lectern to insert a book.
 * If this event is cancelled the player will keep the book and the lectern will remain empty.
 */
@NullMarked
public class PlayerInsertLecternBookEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private boolean cancelled = false;
    private final Block block;
    private ItemStack book;

    @ApiStatus.Internal
    public PlayerInsertLecternBookEvent(final Player player, final Block block, final ItemStack book) {
        super(player);
        this.block = block;
        this.book = book;
    }

    /**
     * Gets the block of the lectern involved in this event.
     *
     * @return the block of the lectern
     */
    public Block getBlock() {
        return this.block;
    }

    /**
     * Fetches the lectern block state that was part of this event.
     * This method constructs a new snapshot {@link org.bukkit.block.BlockState}.
     *
     * @return a new lectern state snapshot of the involved lectern
     * @throws IllegalStateException if the block at {@link #getBlock()} is no longer a lectern
     */
    public Lectern getLectern() {
        final BlockState state = this.getBlock().getState();
        Preconditions.checkState(state instanceof Lectern, "Block state of lectern block is no longer a lectern tile state!");
        return (Lectern) state;
    }

    /**
     * Returns the itemstack the player tried to insert. This is a copy of the item,
     * changes made to this itemstack will not affect the book that is being placed in the lectern.
     * If you want to mutate the item stack that ends up in the lectern, use {@link #setBook(ItemStack)}.
     *
     * @return the book that is being placed
     */
    public ItemStack getBook() {
        return this.book.clone();
    }

    /**
     * Sets the itemstack to insert into the lectern.
     *
     * @param book the book to insert (non book items will leave the lectern in a locked
     *             state as the menu cannot be opened, preventing item extraction)
     */
    public void setBook(final ItemStack book) {
        Preconditions.checkArgument(book != null, "Cannot set book to null");
        this.book = book.clone();
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
