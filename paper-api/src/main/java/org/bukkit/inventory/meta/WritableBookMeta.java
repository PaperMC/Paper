package org.bukkit.inventory.meta;

import java.util.List;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a book ({@link Material#WRITABLE_BOOK} or {@link
 * Material#WRITTEN_BOOK}) that can have pages.
 */
public interface WritableBookMeta extends ItemMeta {

    /**
     * Checks for the existence of pages in the book.
     *
     * @return true if the book has pages
     */
    boolean hasPages();

    /**
     * Gets the specified page in the book. The given page must exist.
     * <p>
     * Pages are 1-indexed.
     *
     * @param page the page number to get, in range [1, getPageCount()]
     * @return the page from the book
     */
    @NotNull
    String getPage(int page);

    /**
     * Sets the specified page in the book. Pages of the book must be
     * contiguous.
     * <p>
     * The data can be up to 1024 characters in length, additional characters
     * are truncated.
     * <p>
     * Pages are 1-indexed.
     *
     * @param page the page number to set, in range [1, getPageCount()]
     * @param data the data to set for that page
     */
    void setPage(int page, @NotNull String data);

    /**
     * Gets all the pages in the book.
     *
     * @return list of all the pages in the book
     */
    @NotNull
    List<String> getPages();

    /**
     * Clears the existing book pages, and sets the book to use the provided
     * pages. Maximum 100 pages with 1024 characters per page.
     *
     * @param pages A list of pages to set the book to use
     */
    void setPages(@NotNull List<String> pages);

    /**
     * Clears the existing book pages, and sets the book to use the provided
     * pages. Maximum 100 pages with 1024 characters per page.
     *
     * @param pages A list of strings, each being a page
     */
    void setPages(@NotNull String... pages);

    /**
     * Adds new pages to the end of the book. Up to a maximum of 100 pages with
     * 1024 characters per page.
     *
     * @param pages A list of strings, each being a page
     */
    void addPage(@NotNull String... pages);

    /**
     * Gets the number of pages in the book.
     *
     * @return the number of pages in the book
     */
    int getPageCount();

    @Override
    @NotNull
    WritableBookMeta clone();
}
