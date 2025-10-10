package io.papermc.paper.jsonrpc;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

/**
 * A handle to a registered notification type that can be used to send notifications
 * to connected management clients.
 *
 * @param <T> The type of data this notification sends (Void for parameterless notifications)
 */
@NullMarked
public interface NotificationHandle<T> {

    /**
     * Gets the namespace of this notification.
     *
     * @return The namespace
     */
    String getNamespace();

    /**
     * Gets the name of this notification.
     *
     * @return The name
     */
    String getName();

    /**
     * Gets the key of this notification.
     *
     * @return The key
     */
    Key key();

    /**
     * Gets the full notification identifier in the format: {@code notification/plugin/<namespace>/<name>}
     *
     * @return The full identifier
     */
    String getFullName();

    /**
     * Gets the description of this notification.
     *
     * @return The description
     */
    String getDescription();

    /**
     * Sends this notification to all connected management clients.
     * <p>
     * For parameterless notifications, pass null as the data parameter.
     * </p>
     *
     * @param data The data to send, or null for parameterless notifications
     * @throws IllegalArgumentException if data is required but null, or if data is the wrong type
     * @throws IllegalStateException if the management server is not available
     */
    void send(@Nullable T data);

    /**
     * Checks if this notification requires data.
     *
     * @return true if data is required when sending
     */
    boolean requiresData();
}
