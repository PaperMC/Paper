package org.bukkit;

import java.net.URI;
import java.util.List;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a collections of links which may be sent to a client.
 */
@ApiStatus.Experimental
public interface ServerLinks {

    /**
     * Gets the link of a given type, if it exists.
     *
     * @param type link type
     * @return link or null
     */
    @Nullable
    ServerLink getLink(@NotNull Type type);

    /**
     * Gets an immutable list of all links.
     *
     * @return immutable list
     */
    @NotNull
    List<ServerLink> getLinks();

    /**
     * Adds the given link, overwriting the first link of the same type if
     * already set.
     *
     * @param type link type
     * @param url link url
     * @return the added link
     */
    @NotNull
    ServerLink setLink(@NotNull Type type, @NotNull URI url);

    /**
     * Adds the given link to the list of links.
     *
     * @param type link type
     * @param url link url
     * @return the added link
     */
    @NotNull
    ServerLink addLink(@NotNull Type type, @NotNull URI url);

    /**
     * Adds the given link to the list of links.
     *
     * @param displayName link name / display text
     * @param url link url
     * @return the added link
     */
    @NotNull
    ServerLink addLink(@NotNull String displayName, @NotNull URI url);

    /**
     * Removes the given link.
     *
     * @param link the link to remove
     * @return if the link existed and was removed
     */
    boolean removeLink(@NotNull ServerLink link);

    /**
     * Returns a copy of this link collection, unassociated from the server.
     *
     * @return copied links
     */
    @NotNull
    ServerLinks copy();

    /**
     * Represents a server link.
     */
    public interface ServerLink {

        /**
         * Gets the type of this link if it is a known special type.
         *
         * @return type or null
         */
        @Nullable
        Type getType();

        /**
         * Gets the display name/text of this link.
         *
         * @return display name
         */
        @NotNull
        String getDisplayName();

        /**
         * Gets the url of this link.
         *
         * @return link url
         */
        @NotNull
        URI getUrl();
    }

    /**
     * Represents a known type of link which will be translated by the client
     * and may have special functionality.
     */
    public enum Type {

        /**
         * Bug report links which may appear on disconnect/crash screens.
         */
        REPORT_BUG,
        COMMUNITY_GUIDELINES,
        SUPPORT,
        STATUS,
        FEEDBACK,
        COMMUNITY,
        WEBSITE,
        FORUMS,
        NEWS,
        ANNOUNCEMENTS;
    }
}
