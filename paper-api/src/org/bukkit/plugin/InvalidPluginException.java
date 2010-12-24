
package org.bukkit.plugin;

/**
 * Thrown when attempting to load an invalid Plugin file
 */
public class InvalidPluginException extends Exception {
    private final Throwable cause;

    /**
     * Constructs a new InvalidPluginException based on the given Exception
     *
     * @param throwable Exception that triggered this Exception
     */
    public InvalidPluginException(Throwable throwable) {
        cause = throwable;
    }

    /**
     * Constructs a new InvalidPluginException
     */
    public InvalidPluginException() {
        cause = null;
    }

    /**
     * If applicable, returns the Exception that triggered this Exception
     *
     * @return Inner exception, or null if one does not exist
     */
    @Override
    public Throwable getCause() {
        return cause;
    }
}
