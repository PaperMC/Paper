package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import io.papermc.paper.text.Filtered;
import java.util.List;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

/**
 * Holds the pages for a writable book.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#WRITABLE_BOOK_CONTENT
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface WritableBookContent {

    @Contract(value = "-> new", pure = true)
    static WritableBookContent.Builder writeableBookContent() {
        return ItemComponentTypesBridge.bridge().writeableBookContent();
    }

    /**
     * Holds the pages that can be written to for this component.
     *
     * @return pages, as filtered objects
     */
    @Contract(pure = true)
    @Unmodifiable List<Filtered<String>> pages();

    /**
     * Builder for {@link WritableBookContent}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<WritableBookContent> {

        /**
         * Adds a page that can be written to for this builder.
         *
         * @param page page
         * @return the builder for chaining
         * @see #pages()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addPage(String page);

        /**
         * Adds pages that can be written to for this builder.
         *
         * @param pages pages
         * @return the builder for chaining
         * @see #pages()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addPages(List<String> pages);

        /**
         * Adds a filterable page that can be written to for this builder.
         *
         * @param page page
         * @return the builder for chaining
         * @see #pages()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addFilteredPage(Filtered<String> page);

        /**
         * Adds filterable pages that can be written to for this builder.
         *
         * @param pages pages
         * @return the builder for chaining
         * @see #pages()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addFilteredPages(List<Filtered<String>> pages);
    }
}
