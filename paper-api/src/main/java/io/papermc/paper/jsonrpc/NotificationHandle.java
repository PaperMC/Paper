package io.papermc.paper.jsonrpc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A handle to a registered notification type that can be used to send notifications
 * to connected management clients.
 *
 * @param <T> The type of data this notification sends (Void for parameterless notifications)
 */
public interface NotificationHandle<T> {

    /**
     * Gets the namespace of this notification.
     *
     * @return The namespace
     */
    @NotNull
    String getNamespace();

    /**
     * Gets the name of this notification.
     *
     * @return The name
     */
    @NotNull
    String getName();

    /**
     * Gets the full notification identifier in the format: {@code notification/plugin/<namespace>/<name>}
     *
     * @return The full identifier
     */
    @NotNull
    String getFullName();

    /**
     * Gets the description of this notification.
     *
     * @return The description
     */
    @NotNull
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
