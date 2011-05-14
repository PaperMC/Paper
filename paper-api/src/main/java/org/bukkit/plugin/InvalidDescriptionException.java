package org.bukkit.plugin;

/**
 * Thrown when attempting to load an invalid PluginDescriptionFile
 */
public class InvalidDescriptionException extends Exception {
    private static final long serialVersionUID = 5721389122281775894L;
    private final Throwable cause;
    private final String message;

    /**
     * Constructs a new InvalidDescriptionException based on the given Exception
     *
     * @param throwable Exception that triggered this Exception
     */
    public InvalidDescriptionException(Throwable throwable) {
        this(throwable, "Invalid plugin.yml");
    }

    /**
     * Constructs a new InvalidDescriptionException with the given message
     *
     * @param message Brief message explaining the cause of the exception
     */
    public InvalidDescriptionException(final String message) {
        this(null, message);
    }

    /**
     * Constructs a new InvalidDescriptionException based on the given Exception
     *
     * @param message Brief message explaining the cause of the exception
     * @param throwable Exception that triggered this Exception
     */
    public InvalidDescriptionException(final Throwable throwable, final String message) {
        this.cause = null;
        this.message = message;
    }

    /**
     * Constructs a new InvalidDescriptionException
     */
    public InvalidDescriptionException() {
        this(null, "Invalid plugin.yml");
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

    @Override
    public String getMessage() {
        return message;
    }
}
