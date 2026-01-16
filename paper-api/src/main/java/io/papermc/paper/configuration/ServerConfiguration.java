package io.papermc.paper.configuration;

/**
 * Represents the configuration settings for a server.
 * <p>
 * This interface doesn't aim to cover every possible server configuration
 * option but focuses on selected critical settings and behaviors.
 */
public interface ServerConfiguration {

    /**
     * Gets whether the server is in online mode.
     * <p>
     * This method returns true if:
     * <ul>
     * <li>The server is in {@link org.bukkit.Server#getOnlineMode online mode},</li>
     * <li>Velocity is enabled and configured to be in online mode, or</li>
     * <li>BungeeCord is enabled and configured to be in online mode.</li>
     * </ul>
     *
     * @return whether the server is in online mode or behind a proxy configured for online mode
     */
    boolean isProxyOnlineMode();

    /**
     * Gets whether the server is configured to work behind a proxy.
     * <p>
     * This returns true if either Velocity or BungeeCord is enabled.
     *
     * @return whether the server is configured to work behind a proxy
     */
    boolean isProxyEnabled();
}
