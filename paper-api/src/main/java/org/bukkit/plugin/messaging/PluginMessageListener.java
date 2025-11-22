package org.bukkit.plugin.messaging;

import io.papermc.paper.connection.PlayerConnection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * A listener for a specific Plugin Channel, which will receive notifications
 * of messages sent from a client.
 */
@FunctionalInterface
public interface PluginMessageListener {

    /**
     * A method that will be thrown when a PluginMessageSource sends a plugin
     * message on a registered channel.
     * <p>
     * Only called for joined players.
     *
     * @param channel Channel that the message was sent through.
     * @param player Source of the message.
     * @param message The raw message that was sent.
     * @see #onPluginMessageReceived(String, PlayerConnection, byte[]) for a method that is called for both joined and configuring players
     */
    void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte @NotNull [] message);

    /**
     * A method that will be invoked when a PluginMessageSource sends a plugin
     * message on a registered channel.
     * <p>
     * Called for both joined players and players in the configuration stage.
     *
     * @param channel Channel that the message was sent through.
     * @param connection Source of the message.
     * @param message The raw message that was sent.
     */
    @ApiStatus.Experimental
    default void onPluginMessageReceived(@NotNull String channel, @NotNull PlayerConnection connection, byte @NotNull [] message) {
    }
}
