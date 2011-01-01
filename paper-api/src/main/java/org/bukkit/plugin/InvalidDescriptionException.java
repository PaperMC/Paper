
package org.bukkit.plugin;

/**
 * Thrown when attempting to load an invalid PluginDescriptionFile
 */
public class InvalidDescriptionException extends Exception {
    private static final long serialVersionUID = 5721389122281775894L;
    private final Throwable cause;

    /**
     * Constructs a new InvalidDescriptionException based on the given Exception
     *
     * @param throwable Exception that triggered this Exception
     */
    public InvalidDescriptionException(Throwable throwable) {
        cause = throwable;
    }

    /**
     * Constructs a new InvalidDescriptionException
     */
    public InvalidDescriptionException() {
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
