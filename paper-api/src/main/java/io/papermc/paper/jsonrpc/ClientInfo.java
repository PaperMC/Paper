package io.papermc.paper.jsonrpc;

/**
 * Information about a connected management client.
 */
public interface ClientInfo {

    /**
     * Gets the unique connection ID for this client.
     *
     * @return The connection ID
     */
    int getConnectionId();
}
