package io.papermc.paper.connection;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.Nullable;

public interface PlayerConnection {

    /**
     * Disconnects the player connection.
     * <p>
     * Note that calling this during connection related events may cause undefined behavior.
     *
     * @param component disconnect reason
     */
    void disconnect(Component component);

    /**
     * {@return whether this connection is currently open and active}
     */
    boolean isConnected();

    /**
     * Gets if this connection originated from a transferred connection.
     * <p>
     * Do note that this is sent and stored on the client.
     *
     * @return is transferred
     */
    boolean isTransferred();

    /**
     * Gets the raw remote address of the connection. This may be a proxy address
     * or a Unix domain socket address, depending on how the channel was established.
     *
     * @return the remote {@link SocketAddress} of the channel
     */
    SocketAddress getAddress();

    /**
     * Gets the real client address of the player. If the connection is behind a proxy,
     * this will be the actual player’s IP address extracted from the proxy handshake.
     *
     * @return the client {@link InetSocketAddress}
     */
    InetSocketAddress getClientAddress();

    /**
     * Returns the virtual host the client is connected to.
     *
     * <p>The virtual host refers to the hostname/port the client used to
     * connect to the server.</p>
     *
     * @return The client's virtual host, or {@code null} if unknown
     */
    @Nullable InetSocketAddress getVirtualHost();

    /**
     * Gets the socket address of this player's proxy
     *
     * @return the player's proxy address, null if the server doesn't have Proxy Protocol enabled, or the player didn't connect to an HAProxy instance
     */
    @Nullable InetSocketAddress getHAProxyAddress();

    /**
     * Gets a stable identifier for this connection that remains the same
     * throughout its entire lifecycle, from the initial handshake until the
     * connection is closed or the player fully joins the server.
     *
     * <p>This identifier is unique per connection and can be used to reliably
     * correlate data collected during earlier connection stages (e.g. handshake
     * or login) with the player that eventually joins the server, without
     * relying on the client's IP address.</p>
     *
     * @return the stable connection identifier
     */
    UUID getConnectionId();

}
