package com.destroystokyo.paper.exception;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import static com.google.common.base.Preconditions.*;

/**
 * Thrown when an incoming plugin message channel throws an exception
 */
public class ServerPluginMessageException extends ServerPluginException {

    private final Player player;
    private final String channel;
    private final byte[] data;

    public ServerPluginMessageException(String message, Throwable cause, Plugin responsiblePlugin, Player player, String channel, byte[] data) {
        super(message, cause, responsiblePlugin);
        this.player = checkNotNull(player, "player");
        this.channel = checkNotNull(channel, "channel");
        this.data = checkNotNull(data, "data");
    }

    public ServerPluginMessageException(Throwable cause, Plugin responsiblePlugin, Player player, String channel, byte[] data) {
        super(cause, responsiblePlugin);
        this.player = checkNotNull(player, "player");
        this.channel = checkNotNull(channel, "channel");
        this.data = checkNotNull(data, "data");
    }

    protected ServerPluginMessageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Plugin responsiblePlugin, Player player, String channel, byte[] data) {
        super(message, cause, enableSuppression, writableStackTrace, responsiblePlugin);
        this.player = checkNotNull(player, "player");
        this.channel = checkNotNull(channel, "channel");
        this.data = checkNotNull(data, "data");
    }

    /**
     * Gets the channel to which the error occurred from receiving data from
     *
     * @return exception channel
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Gets the data to which the error occurred from
     *
     * @return exception data
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Gets the player which the plugin message causing the exception originated from
     *
     * @return exception player
     */
    public Player getPlayer() {
        return player;
    }
}
