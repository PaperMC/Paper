package org.bukkit.inventory.view;

import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.LecternInventory;
import org.jetbrains.annotations.NotNull;

/**
 * An instance of {@link InventoryView} which provides extra methods related to
 * lectern view data.
 */
public interface LecternView extends InventoryView {

    @NotNull
    @Override
    LecternInventory getTopInventory();

    /**
     * Gets the page that the LecternView is on.
     *
     * @return The page the book is on
     */
    int getPage();

    /**
     * Sets the page of the lectern book.
     *
     * @param page the lectern book page
     */
    void setPage(final int page);
}
