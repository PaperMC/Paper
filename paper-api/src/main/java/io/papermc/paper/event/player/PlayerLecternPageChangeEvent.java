package io.papermc.paper.event.player;

import org.bukkit.block.Lectern;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * @since 1.16.4
 */
@NullMarked
public class PlayerLecternPageChangeEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Lectern lectern;
    private final ItemStack book;
    private final PageChangeDirection pageChangeDirection;
    private final int oldPage;
    private int newPage;

    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerLecternPageChangeEvent(final Player player, final Lectern lectern, final ItemStack book, final PageChangeDirection pageChangeDirection, final int oldPage, final int newPage) {
        super(player);
        this.lectern = lectern;
        this.book = book;
        this.pageChangeDirection = pageChangeDirection;
        this.oldPage = oldPage;
        this.newPage = newPage;
    }

    /**
     * Gets the lectern involved.
     *
     * @return the Lectern
     */
    public Lectern getLectern() {
        return this.lectern;
    }

    /**
     * Gets the current ItemStack on the lectern.
     *
     * @return the ItemStack on the Lectern
     */
    public ItemStack getBook() {
        return this.book;
    }

    /**
     * Gets the page change direction. This is essentially returns which button the player clicked, left or right.
     *
     * @return the page change direction
     */
    public PageChangeDirection getPageChangeDirection() {
        return this.pageChangeDirection;
    }

    /**
     * Gets the page changed from. <i>Pages are 0-indexed.</i>
     *
     * @return the page changed from
     */
    public int getOldPage() {
        return this.oldPage;
    }

    /**
     * Gets the page changed to. <i>Pages are 0-indexed.</i>
     *
     * @return the page changed to
     */
    public int getNewPage() {
        return this.newPage;
    }

    /**
     * Sets the page changed to. <i>Pages are 0-indexed.</i>
     * Page indices that are greater than the number of pages will show the last page.
     *
     * @param newPage the new paged changed to
     */
    public void setNewPage(final int newPage) {
        this.newPage = newPage;
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

    public enum PageChangeDirection {
        LEFT,
        RIGHT,
    }
}
