package io.papermc.paper.configuration;

/// Represents the configuration settings for a server.
///
/// This interface doesn't aim to cover every possible server configuration
/// option but focuses on selected critical settings and behaviors.
public interface ServerConfiguration {

    /// Gets whether the server is in online mode.
    ///
    /// This method returns true if:
    ///
    ///   - The server is in [`online mode`][org.bukkit.Server#getOnlineMode],
    ///   - Velocity is enabled and configured to be in online mode, or
    ///   - BungeeCord is enabled and configured to be in online mode.
    ///
    /// @return whether the server is in online mode or behind a proxy configured for online mode
    boolean isProxyOnlineMode();

    /// Gets whether the server is configured to work behind a proxy.
    ///
    /// This returns true if either Velocity or BungeeCord is enabled.
    ///
    /// @return whether the server is configured to work behind a proxy
    boolean isProxyEnabled();
}
