package org.bukkit.plugin.messaging;

/**
 * Thrown if a Plugin Channel is too long.
 */
@SuppressWarnings("serial")
public class ChannelNameTooLongException extends RuntimeException {
    public ChannelNameTooLongException() {
        super("Attempted to send a Plugin Message to a channel that was too large. The maximum length a channel may be is " + Messenger.MAX_CHANNEL_SIZE + " chars.");
    }

    // Paper start
    public ChannelNameTooLongException(int length, String shortenedChannel) {
        super("Attempted to send a Plugin Message to a channel that was too large. The maximum length a channel may be is " + Messenger.MAX_CHANNEL_SIZE + " chars (attempted " + length + " - '" + shortenedChannel + ".");
        // Paper end
    }
}
