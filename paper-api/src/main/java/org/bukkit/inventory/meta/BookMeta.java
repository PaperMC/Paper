package org.bukkit.inventory.meta;

import java.util.List;

import org.bukkit.Material;

/**
 * Represents a book ({@link Material#BOOK_AND_QUILL} or {@link Material#WRITTEN_BOOK}) that can have a title, an author, and pages.
 */
public interface BookMeta extends ItemMeta {

    /**
     * Checks for the existence of a title in the book.
     *
     * @return true if the book has a title
     */
    boolean hasTitle();

    /**
     * Gets the title of the book.
     *
     * @return the title of the book
     */
    String getTitle();

    /**
     * Sets the title of the book. Limited to 16 characters.
     *
     * @param title the title to set
     * @return true if the title was successfully set
     */
    boolean setTitle(String title);

    /**
     * Checks for the existence of an author in the book.
     *
     * @return the author of the book
     */
    boolean hasAuthor();

    /**
     * Gets the author of the book.
     *
     * @return the author of the book
     */
    String getAuthor();

    /**
     * Sets the author of the book.
     *
     * @param author the author of the book
     */
    void setAuthor(String author);

    /**
     * Checks for the existence of pages in the book.
     *
     * @return true if the book has pages
     */
    boolean hasPages();

    /**
     * Gets the specified page in the book.
     *
     * @param page the page number to get
     * @return the page from the book
     */
    String getPage(int page);

    /**
     * Sets the specified page in the book.
     *
     * @param page the page number to set
     * @param data the data to set for that page
     */
    void setPage(int page, String data);

    /**
     * Gets all the pages in the book.
     *
     * @return list of all the pages in the book
     */
    List<String> getPages();

    /**
     * Clears the existing book pages, and sets the book to use the provided pages. Maximum 50 pages with 256 characters per page.
     *
     * @param pages A list of pages to set the book to use
     */
    void setPages(List<String> pages);

    /**
     * Clears the existing book pages, and sets the book to use the provided pages. Maximum 50 pages with 256 characters per page.
     *
     * @param pages A list of strings, each being a page
     */
    void setPages(String... pages);

    /**
     * Adds new pages to the end of the book.
     *
     * @param pages A list of strings, each being a page
     */
    void addPage(String... pages);

    /**
     * Gets the number of pages in the book.
     *
     * @return the number of pages in the book
     */
    int getPageCount();

    BookMeta clone();
}
