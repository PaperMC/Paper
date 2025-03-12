package io.papermc.paper.configuration;

public interface ServerConfiguration {

    /**
     * Gets whether the Server is behind a proxy that uses online mode.
     *
     * @return true if the server is in proxied online mode, false otherwise
     */
    boolean isProxyOnlineMode();

    /**
     * Gets whether the Server is configured for a Velocity proxy
     *
     * @return true if the server has Velocity enabled; false otherwise.
     */
    boolean isVelocityEnabled();
    /**
     * Gets whether the Server is configured for BungeeCord online mode
     *
     * @return true if the server is in BungeeCord online mode; false otherwise.
     */
    boolean isBungeeCordEnabled();

    /**
     * Gets whether the Server is configured for Velocity online mode
     *
     * @return true if the server is in Velocity online mode; false otherwise.
     */
    boolean isVelocityOnlineMode();

    /**
     * Gets whether the Server is configured for a BungeeCord proxy
     *
     * @return true if the server has BungeeCord enabled; false otherwise.
     */
    boolean isBungeeCordOnlineMode();
}
