package org.bukkit.inventory;

import org.bukkit.block.Lectern;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Interface to the inventory of a Lectern.
 */
@NullMarked
public interface LecternInventory extends Inventory {

    @Override
    @Nullable Lectern getHolder();

    /**
     * Gets the lectern's held book.
     *
     * @return book set in the lectern
     */
    default @Nullable ItemStack getBook() {
        return getItem(0);
    }

    /**
     * Sets the lectern's held book.
     *
     * @param book the new book
     */
    default void setBook(final @Nullable ItemStack book) {
        this.setItem(0, book);
    }
}
