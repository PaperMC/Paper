package io.papermc.paper.connection;

import java.util.Map;

import com.destroystokyo.paper.ClientOption;
import org.bukkit.ServerLinks;
import org.bukkit.plugin.messaging.PluginMessageRecipient;

/**
 * Represents a connection that has properties shared between the GAME and CONFIG stage.
 */
public interface PlayerCommonConnection extends WritablePlayerCookieConnection, ReadablePlayerCookieConnection, PluginMessageRecipient {

    /**
     * Sends data to appear in this connection's report logs.
     * This is useful for debugging server state that may be causing
     * player disconnects.
     * <p>
     * These are formatted as key - value, where keys are limited to a length of 128 characters,
     * values are limited to 4096, and 32 maximum entries can be sent.
     *
     * @param details report details
     */
    void sendReportDetails(Map<String, String> details);

    /**
     * Sends the given server links to this connection.
     *
     * @param links links to send
     */
    void sendLinks(ServerLinks links);

    /**
     * Transfers this connection to another server.
     *
     * @param host host
     * @param port port
     */
    void transfer(String host, int port);

    /**
     * @param type client option
     * @return the client option value of the player
     */
    <T> T getClientOption(ClientOption<T> type);
}
