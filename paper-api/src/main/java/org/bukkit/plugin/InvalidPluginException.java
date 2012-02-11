package org.bukkit.plugin;

/**
 * Thrown when attempting to load an invalid Plugin file
 */
public class InvalidPluginException extends Exception {
    private static final long serialVersionUID = -8242141640709409542L;

    /**
     * Constructs a new InvalidPluginException based on the given Exception
     *
     * @param cause Exception that triggered this Exception
     */
    public InvalidPluginException(final Throwable cause) {
        super("Invalid plugin" + (cause != null ? ": " + cause.getMessage() : ""), cause);
    }

    /**
     * Constructs a new InvalidPluginException
     */
    public InvalidPluginException() {
        this(null);
    }
}
