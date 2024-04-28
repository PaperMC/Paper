package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import io.papermc.paper.text.Filtered;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.checkerframework.common.value.qual.IntRange;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

/**
 * Holds the contents and metadata of a Written Book.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#WRITTEN_BOOK_CONTENT
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface WrittenBookContent {

    @Contract(value = "_, _ -> new", pure = true)
    static WrittenBookContent.Builder writtenBookContent(final String title, final String author) {
        return writtenBookContent(Filtered.of(title, null), author);
    }

    @Contract(value = "_, _ -> new", pure = true)
    static WrittenBookContent.Builder writtenBookContent(final Filtered<String> title, final String author) {
        return ItemComponentTypesBridge.bridge().writtenBookContent(title, author);
    }

    /**
     * Title of this book.
     *
     * @return title
     */
    @Contract(pure = true)
    Filtered<String> title();

    /**
     * Player name of the author of this book.
     *
     * @return author
     */
    @Contract(pure = true)
    String author();

    /**
     * The number of times this book has been copied (0 = original).
     *
     * @return generation
     */
    @Contract(pure = true)
    @IntRange(from = 0, to = 3) int generation();

    /**
     * Gets the pages of this book.
     *
     * @return pages
     */
    @Contract(pure = true)
    @Unmodifiable List<Filtered<Component>> pages();

    /**
     * If the chat components in this book have already been resolved (entity selectors, scores substituted).
     * If {@code false}, will be resolved when opened by a player.
     *
     * @return resolved
     */
    @Contract(pure = true)
    boolean resolved();

    /**
     * Builder for {@link WrittenBookContent}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<WrittenBookContent> {

        /**
         * Sets the title of this book.
         *
         * @param title the title
         * @return the builder for chaining
         * @see #title()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder title(String title);

        /**
         * Sets the filterable title of this book.
         *
         * @param title the title
         * @return the builder for chaining
         * @see #title()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder filteredTitle(Filtered<String> title);

        /**
         * Sets the author of this book.
         *
         * @param author the author
         * @return the builder for chaining
         * @see #author()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder author(String author);

        /**
         * Sets the generation of this book.
         *
         * @param generation the generation, [0-3]
         * @return the builder for chaining
         * @see #generation()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder generation(@IntRange(from = 0, to = 3) int generation);

        /**
         * Sets if the chat components in this book have already been resolved (entity selectors, scores substituted).
         * If {@code false}, will be resolved when opened by a player.
         *
         * @param resolved resolved
         * @return the builder for chaining
         * @see #resolved()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder resolved(boolean resolved);

        /**
         * Adds a page to this book.
         *
         * @param page the page
         * @return the builder for chaining
         * @see #pages()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addPage(ComponentLike page);

        /**
         * Adds pages to this book.
         *
         * @param page the pages
         * @return the builder for chaining
         * @see #pages()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addPages(List<? extends ComponentLike> page);

        /**
         * Adds a filterable page to this book.
         *
         * @param page the page
         * @return the builder for chaining
         * @see #pages()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addFilteredPage(Filtered<? extends ComponentLike> page);

        /**
         * Adds filterable pages to this book.
         *
         * @param pages the pages
         * @return the builder for chaining
         * @see #pages()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addFilteredPages(List<Filtered<? extends ComponentLike>> pages);
    }
}
