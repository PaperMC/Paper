package org.bukkit.plugin.messaging;

/**
 * Thrown if a Plugin attempts to send a message on an unregistered channel.
 *
 * @since 1.1.0
 */
@SuppressWarnings("serial")
public class ChannelNotRegisteredException extends RuntimeException {
    public ChannelNotRegisteredException() {
        this("Attempted to send a plugin message through an unregistered channel.");
    }

    public ChannelNotRegisteredException(String channel) {
        super("Attempted to send a plugin message through the unregistered channel `" + channel + "'.");
    }
}
