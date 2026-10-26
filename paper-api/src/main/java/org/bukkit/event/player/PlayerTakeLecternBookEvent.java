package org.bukkit.event.player;

import org.bukkit.block.Lectern;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.LecternInventory;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * This event is called when a player clicks the button to take a book of a
 * Lectern. If this event is cancelled the book remains on the lectern.
 */
@NullMarked
public class PlayerTakeLecternBookEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final LecternInventory inventory;
    private final @Nullable Lectern lectern;
    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerTakeLecternBookEvent(final Player player, final LecternInventory inventory, final @Nullable Lectern lectern) {
        super(player);
        this.inventory = inventory;
        this.lectern = lectern;
    }

    /**
     * Gets the inventory of the lectern.
     *
     * @return the Inventory of the Lectern
     */
    public LecternInventory getInventory() {
        return this.inventory;
    }

    /**
     * Gets the lectern involved.
     *
     * @return the Lectern
     */
    public @Nullable Lectern getLectern() {
        return this.lectern;
    }

    /**
     * Gets the current ItemStack on the lectern.
     *
     * @return the ItemStack on the Lectern
     */
    public @Nullable ItemStack getBook() {
        return this.getInventory().getItem(0);
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
