
package org.bukkit.plugin;

/**
 * Thrown when attempting to load an invalid PluginDescriptionFile
 */
public class InvalidDescriptionException extends Exception {
    private final Exception innerException;

    /**
     * Constructs a new InvalidDescriptionException based on the given Exception
     *
     * @param exception Exception that triggered this Exception
     */
    public InvalidDescriptionException(Exception exception) {
        innerException = exception;
    }

    /**
     * Constructs a new InvalidDescriptionException
     */
    public InvalidDescriptionException() {
        innerException = null;
    }

    /**
     * If applicable, returns the Exception that triggered this InvalidDescriptionException
     *
     * @return Inner exception, or null if one does not exist
     */
    public Exception getInnerException() {
        return innerException;
    }
}
