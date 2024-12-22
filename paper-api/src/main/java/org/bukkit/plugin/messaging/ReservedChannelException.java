package org.bukkit.plugin.messaging;

/**
 * Thrown if a plugin attempts to register for a reserved channel (such as
 * "REGISTER")
 *
 * @since 1.1.0 R1
 */
@SuppressWarnings("serial")
public class ReservedChannelException extends RuntimeException {
    public ReservedChannelException() {
        this("Attempted to register for a reserved channel name.");
    }

    public ReservedChannelException(String name) {
        super("Attempted to register for a reserved channel name ('" + name + "')");
    }
}
