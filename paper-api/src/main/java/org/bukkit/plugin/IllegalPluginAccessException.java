package org.bukkit.plugin;

/// Thrown when a plugin attempts to interact with the server when it is not
/// enabled
@SuppressWarnings("serial")
public class IllegalPluginAccessException extends RuntimeException {

    /// Creates a new instance of `IllegalPluginAccessException`
    /// without detail message.
    public IllegalPluginAccessException() {}

    /// Constructs an instance of `IllegalPluginAccessException`
    /// with the specified detail message.
    ///
    /// @param msg the detail message.
    public IllegalPluginAccessException(String msg) {
        super(msg);
    }
}
