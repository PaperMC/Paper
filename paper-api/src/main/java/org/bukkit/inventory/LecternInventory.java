package org.bukkit.inventory;

import org.bukkit.block.Lectern;
import org.jetbrains.annotations.Nullable;

/**
 * Interface to the inventory of a Lectern.
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
     */
    @Nullable
    default ItemStack getBook() {
        return getItem(0);
    }

    /**
     * Sets the lectern's held book.
     *
     * @param book the new book
     */
    default void setBook(@Nullable ItemStack book) {
        setItem(0, book);
    }
    // Paper end
}
