package org.bukkit.block;

import org.jspecify.annotations.NullMarked;

/**
 * Represents a captured state of a lectern.
 */
@NullMarked
public interface Lectern extends io.papermc.paper.block.TileStateInventoryHolder {

    /**
     * Get the current lectern page.
     *
     * @return current page
     */
    int getPage();

    /**
     * Set the current lectern page.
     * <br>
     * If the page is greater than the number of pages of the book currently in
     * the inventory, then behavior is undefined.
     *
     * @param page new page
     */
    void setPage(int page);
}
