package io.papermc.paper.configuration;

/**
 * Represents the configuration settings for a server.
 * <p>
 * This interface doesn't aim to cover every possible server configuration
 * option but focuses on selected critical settings.
 */
public interface ServerConfiguration {

    /**
     * Gets whether the server is in online mode.
     * <p>
     * This method returns true if:
     * <ul>
     * <li>The server is in {@link org.bukkit.Server#getOnlineMode online mode},</li>
     * <li>Velocity is enabled and configured to be in {@link #isVelocityOnlineMode() online mode}, or</li>
     * <li>BungeeCord is enabled and configured to be in {@link #isBungeeCordOnlineMode() online mode}.</li>
     * </ul>
     *
     * @return whether the server is in online mode or behind a proxy configured for online mode
     */
    boolean isProxyOnlineMode();

    /**
     * Gets whether the server is configured for a Velocity proxy.
     *
     * @return true if the server has Velocity enabled, false otherwise
     */
    boolean isVelocityEnabled();

    /**
     * Gets whether the server is configured for a BungeeCord proxy.
     *
     * @return true if the server has BungeeCord enabled, false otherwise
     */
    boolean isBungeeCordEnabled();

    /**
     * Gets whether the server is configured to operate in Velocity online mode.
     *
     * @return true if the server is in Velocity online mode and Velocity is enabled, false otherwise
     */
    boolean isVelocityOnlineMode();

    /**
     * Gets whether the server is configured to operate in BungeeCord online mode.
     *
     * @return true if the server is in BungeeCord online mode and BungeeCord is enabled, false otherwise
     */
    boolean isBungeeCordOnlineMode();
}
