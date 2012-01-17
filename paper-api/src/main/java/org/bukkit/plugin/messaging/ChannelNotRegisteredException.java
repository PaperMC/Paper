package org.bukkit.plugin.messaging;

/**
 * Thrown if a Plugin attempts to send a message on an unregistered channel.
 */
@SuppressWarnings("serial")
public class ChannelNotRegisteredException extends RuntimeException {
    public ChannelNotRegisteredException() {
        this("Attempted to send a plugin message through an unregistered channel.");
    }

    public ChannelNotRegisteredException(String channel) {
        super("Attempted to send a plugin message through an unregistered channel ('" + channel + "'.");
    }
}
