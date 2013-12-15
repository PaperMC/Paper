package org.bukkit.plugin.messaging;

/**
 * Represents the different directions a plugin channel may go.
 */
public enum PluginChannelDirection {

    /**
     * The plugin channel is being sent to the server from a client.
     */
    INCOMING,

    /**
     * The plugin channel is being sent to a client from the server.
     */
    OUTGOING
}
