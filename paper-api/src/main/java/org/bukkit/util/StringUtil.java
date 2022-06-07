package org.bukkit.util;

import com.google.common.base.Preconditions;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;

public class StringUtil {

    /**
     * Copies all elements from the iterable collection of originals to the
     * collection provided.
     *
     * @param <T> the collection of strings
     * @param token String to search for
     * @param originals An iterable collection of strings to filter.
     * @param collection The collection to add matches to
     * @return the collection provided that would have the elements copied
     *     into
     * @throws UnsupportedOperationException if the collection is immutable
     *     and originals contains a string which starts with the specified
     *     search string.
     * @throws IllegalArgumentException if any parameter is is null
     * @throws IllegalArgumentException if originals contains a null element.
     *     <b>Note: the collection may be modified before this is thrown</b>
     */
    @NotNull
    public static <T extends Collection<? super String>> T copyPartialMatches(@NotNull final String token, @NotNull final Iterable<String> originals, @NotNull final T collection) throws UnsupportedOperationException, IllegalArgumentException {
        Preconditions.checkArgument(token != null, "Search token cannot be null");
        Preconditions.checkArgument(collection != null, "Collection cannot be null");
        Preconditions.checkArgument(originals != null, "Originals cannot be null");

        for (String string : originals) {
            if (startsWithIgnoreCase(string, token)) {
                collection.add(string);
            }
        }

        return collection;
    }

    /**
     * This method uses a region to check case-insensitive equality. This
     * means the internal array does not need to be copied like a
     * toLowerCase() call would.
     *
     * @param string String to check
     * @param prefix Prefix of string to compare
     * @return true if provided string starts with, ignoring case, the prefix
     *     provided
     * @throws NullPointerException if prefix is null
     * @throws IllegalArgumentException if string is null
     */
    public static boolean startsWithIgnoreCase(@NotNull final String string, @NotNull final String prefix) throws IllegalArgumentException, NullPointerException {
        Preconditions.checkArgument(string != null, "Cannot check a null string for a match");
        if (string.length() < prefix.length()) {
            return false;
        }
        return string.regionMatches(true, 0, prefix, 0, prefix.length());
    }
}
