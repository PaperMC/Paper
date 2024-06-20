package io.papermc.paper.tag;

import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * An entry is a pre-flattened tag. Represents
 * either an individual registry entry or a whole tag.
 *
 * @param <T> the type of value in the tag
 * @see PreFlattenTagRegistrar
 */
@ApiStatus.Experimental
@NullMarked
@ApiStatus.NonExtendable
public interface TagEntry<T> extends Keyed {

    /**
     * Create required tag entry for a single value.
     *
     * @param entryKey the key of the entry
     * @return a new tag entry for a value
     * @param <T> the type of value
     */
    @Contract(value = "_ -> new", pure = true)
    static <T> TagEntry<T> valueEntry(final TypedKey<T> entryKey) {
        return valueEntry(entryKey, true);
    }

    /**
     * Create tag entry for a single value.
     *
     * @param entryKey the key of the entry
     * @param isRequired if this entry is required (see {@link #isRequired()})
     * @return a new tag entry for a value
     * @param <T> the type of value
     */
    @Contract(value = "_, _ -> new", pure = true)
    static <T> TagEntry<T> valueEntry(final TypedKey<T> entryKey, final boolean isRequired) {
        return new TagEntryImpl<>(entryKey.key(), false, isRequired);
    }

    /**
     * Create a required tag entry for a nested tag.
     *
     * @param tagKey they key for the tag
     * @return a new tag entry for a tag
     * @param <T> the type of value
     */
    @Contract(value = "_ -> new", pure = true)
    static <T> TagEntry<T> tagEntry(final TagKey<T> tagKey) {
        return tagEntry(tagKey, true);
    }

    /**
     * Create a tag entry for a nested tag.
     *
     * @param tagKey they key for the tag
     * @param isRequired if this entry is required (see {@link #isRequired()})
     * @return a new tag entry for a tag
     * @param <T> the type of value
     */
    @Contract(value = "_, _ -> new", pure = true)
    static <T> TagEntry<T> tagEntry(final TagKey<T> tagKey, final boolean isRequired) {
        return new TagEntryImpl<>(tagKey.key(), true, isRequired);
    }

    /**
     * Returns if this entry represents a tag.
     *
     * @return true if this entry is a tag, false if it is an individual entry
     */
    @Contract(pure = true)
    boolean isTag();

    /**
     * Returns if this entry is required. If an entry is required,
     * the value or tag must exist on the server in order for the tag
     * to load correctly. A missing value will prevent the tag holding
     * that missing value from being created.
     *
     * @return true if this entry is required, false if it is optional
     */
    @Contract(pure = true)
    boolean isRequired();
}
