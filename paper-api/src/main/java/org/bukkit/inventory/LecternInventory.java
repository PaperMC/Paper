package org.bukkit.inventory;

import org.bukkit.block.Lectern;
import org.jetbrains.annotations.Nullable;

/**
 * Interface to the inventory of a Lectern.
 *
 * @since 1.14
 */
public interface LecternInventory extends Inventory {

    @Nullable
    @Override
    public Lectern getHolder();

    // Paper start
    /**
     * Gets the lectern's held book.
     *
     * @return book set in the lectern
     * @since 1.15.2
     */
    @Nullable
    default ItemStack getBook() {
        return getItem(0);
    }

    /**
     * Sets the lectern's held book.
     *
     * @param book the new book
     * @since 1.15.2
     */
    default void setBook(@Nullable ItemStack book) {
        setItem(0, book);
    }
    // Paper end
}
