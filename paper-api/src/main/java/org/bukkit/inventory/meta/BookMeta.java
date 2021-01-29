package org.bukkit.inventory.meta;

import java.util.List;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a {@link Material#WRITTEN_BOOK} that can have a title, an author,
 * and pages.
 * <p>
 * Before using this type, make sure to check the itemstack's material with
 * {@link org.bukkit.inventory.ItemStack#getType()}. {@code instanceof} on
 * the meta instance is not sufficient due to unusual inheritance
 * with relation to {@link WritableBookMeta}.
 */
public interface BookMeta extends WritableBookMeta, net.kyori.adventure.inventory.Book { // Paper - adventure

    /**
     * Represents the generation (or level of copying) of a written book
     */
    enum Generation {
        /**
         * Book written into a book-and-quill. Can be copied. (Default value)
         */
        ORIGINAL,
        /**
         * Book that was copied from an original. Can be copied.
         */
        COPY_OF_ORIGINAL,
        /**
         * Book that was copied from a copy of an original. Can't be copied.
         */
        COPY_OF_COPY,
        /**
         * Unused; unobtainable by players. Can't be copied.
         */
        TATTERED;
    }

    /**
     * Checks for the existence of a title in the book.
     *
     * @return true if the book has a title
     */
    boolean hasTitle();

    /**
     * Gets the title of the book.
     * <p>
     * Plugins should check that hasTitle() returns true before calling this
     * method.
     *
     * @return the title of the book
     */
    @Nullable
    String getTitle();

    /**
     * Sets the title of the book.
     * <p>
     * Limited to 32 characters. Removes title when given null.
     *
     * @param title the title to set
     * @return true if the title was successfully set
     */
    boolean setTitle(@Nullable String title);

    /**
     * Checks for the existence of an author in the book.
     *
     * @return true if the book has an author
     */
    boolean hasAuthor();

    /**
     * Gets the author of the book.
     * <p>
     * Plugins should check that hasAuthor() returns true before calling this
     * method.
     *
     * @return the author of the book
     */
    @Nullable
    String getAuthor();

    /**
     * Sets the author of the book. Removes author when given null.
     *
     * @param author the author to set
     */
    void setAuthor(@Nullable String author);

    /**
     * Checks for the existence of generation level in the book.
     *
     * @return true if the book has a generation level
     */
    boolean hasGeneration();

    /**
     * Gets the generation of the book.
     * <p>
     * Plugins should check that hasGeneration() returns true before calling
     * this method.
     *
     * @return the generation of the book
     */
    @Nullable
    Generation getGeneration();

    /**
     * Sets the generation of the book. Removes generation when given null.
     *
     * @param generation the generation to set
     */
    void setGeneration(@Nullable Generation generation);

    @Override
    @NotNull
    BookMeta clone();

    // Paper start - adventure
    //<editor-fold desc="deprecations" defaultstate="collapsed">
    /**
     * @deprecated use {@link #page(int)}
     */
    @Deprecated
    @Override
    @NotNull String getPage(int page);

    /**
     * @deprecated use {@link #page(int, net.kyori.adventure.text.Component)}
     */
    @Deprecated
    @Override
    void setPage(int page, @NotNull String data);

    /**
     * @deprecated use {@link #pages()}
     */
    @Deprecated
    @Override
    @NotNull List<String> getPages();

    /**
     * @deprecated use {@link #pages(List)}
     */
    @Deprecated
    @Override
    void setPages(@NotNull List<String> pages);

    /**
     * @deprecated use {@link #pages(net.kyori.adventure.text.Component...)}
     */
    @Deprecated
    @Override
    void setPages(@NotNull String... pages);

    /**
     * @deprecated use {@link #addPages(net.kyori.adventure.text.Component...)}
     */
    @Deprecated
    @Override
    void addPage(@NotNull String... pages);
    //</editor-fold>

    /**
     * Gets the title of the book.
     * <p>
     * Plugins should check that hasTitle() returns true before calling this
     * method.
     *
     * @return the title of the book
     */
    @Override
    net.kyori.adventure.text.@Nullable Component title();

    /**
     * Sets the title of the book.
     * <p>
     * Limited to 32 characters. Removes title when given null.
     *
     * @param title the title to set
     * @return the same {@link BookMeta} instance
     */
    @org.jetbrains.annotations.Contract(value = "_ -> this", pure = false)
    @Override
    @NotNull BookMeta title(net.kyori.adventure.text.@Nullable Component title);

    /**
     * Gets the author of the book.
     * <p>
     * Plugins should check that hasAuthor() returns true before calling this
     * method.
     *
     * @return the author of the book
     */
    @Override
    net.kyori.adventure.text.@Nullable Component author();

    /**
     * Sets the author of the book. Removes author when given null.
     *
     * @param author the author to set
     * @return the same {@link BookMeta} instance
     */
    @org.jetbrains.annotations.Contract(value = "_ -> this", pure = false)
    @Override
    @NotNull BookMeta author(net.kyori.adventure.text.@Nullable Component author);


    /**
     * Gets the specified page in the book. The page must exist.
     * <p>
     * Pages are 1-indexed.
     *
     * @param page the page number to get, in range [1, getPageCount()]
     * @return the page from the book
     */
    net.kyori.adventure.text.@NotNull Component page(int page);

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
    void page(int page, net.kyori.adventure.text.@NotNull Component data);

    /**
     * Adds new pages to the end of the book. Up to a maximum of 100 pages with
     * 1024 characters per page.
     *
     * @param pages A list of strings, each being a page
     */
    void addPages(net.kyori.adventure.text.@NotNull Component @NotNull ... pages);

    interface BookMetaBuilder extends net.kyori.adventure.inventory.Book.Builder {

        @Override
        @NotNull BookMetaBuilder title(net.kyori.adventure.text.@Nullable Component title);

        @Override
        @NotNull BookMetaBuilder author(net.kyori.adventure.text.@Nullable Component author);

        @Override
        @NotNull BookMetaBuilder addPage(net.kyori.adventure.text.@NotNull Component page);

        @Override
        @NotNull BookMetaBuilder pages(net.kyori.adventure.text.@NotNull Component @NotNull ... pages);

        @Override
        @NotNull BookMetaBuilder pages(java.util.@NotNull Collection<net.kyori.adventure.text.Component> pages);

        @Override
        @NotNull BookMeta build();
    }

    @Override
    @NotNull BookMetaBuilder toBuilder();
    // Paper end

    // Spigot start
    public class Spigot {

        /**
         * Gets the specified page in the book. The given page must exist.
         *
         * @param page the page number to get
         * @return the page from the book
         * @deprecated in favour of {@link #page(int)}
         */
        @NotNull
        @Deprecated // Paper
        public BaseComponent[] getPage(int page) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * Sets the specified page in the book. Pages of the book must be
         * contiguous.
         * <p>
         * The data can be up to 256 characters in length, additional characters
         * are truncated.
         *
         * @param page the page number to set
         * @param data the data to set for that page
         * @deprecated in favour of {@link #page(int, net.kyori.adventure.text.Component)}
         */
        @Deprecated // Paper
        public void setPage(int page, @Nullable BaseComponent... data) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * Gets all the pages in the book.
         *
         * @return list of all the pages in the book
         * @deprecated in favour of {@link #pages()}
         */
        @NotNull
        @Deprecated // Paper
        public List<BaseComponent[]> getPages() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * Clears the existing book pages, and sets the book to use the provided
         * pages. Maximum 50 pages with 256 characters per page.
         *
         * @param pages A list of pages to set the book to use
         * @deprecated in favour of {@link #pages(java.util.List)}
         */
        @Deprecated // Paper
        public void setPages(@NotNull List<BaseComponent[]> pages) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * Clears the existing book pages, and sets the book to use the provided
         * pages. Maximum 50 pages with 256 characters per page.
         *
         * @param pages A list of component arrays, each being a page
         * @deprecated in favour of {@link #pages(net.kyori.adventure.text.Component...)}
         */
        @Deprecated // Paper
        public void setPages(@NotNull BaseComponent[]... pages) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * Adds new pages to the end of the book. Up to a maximum of 50 pages
         * with 256 characters per page.
         *
         * @param pages A list of component arrays, each being a page
         * @deprecated in favour of {@link #addPages(net.kyori.adventure.text.Component...)}
         */
        @Deprecated // Paper
        public void addPage(@NotNull BaseComponent[]... pages) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    @NotNull
    Spigot spigot();
    // Spigot end
}
