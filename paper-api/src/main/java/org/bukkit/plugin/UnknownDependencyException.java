
package org.bukkit.plugin;

/**
 * Thrown when attempting to load an invalid Plugin file
 */
public class UnknownDependencyException extends Exception {

    private static final long serialVersionUID = 5721389371901775894L;
    private final Throwable cause;
    private final String message;

    /**
     * Constructs a new UnknownDependencyException based on the given Exception
     *
     * @param throwable Exception that triggered this Exception
     */
    public UnknownDependencyException(Throwable throwable) {
        this(throwable, "Unknown dependency");
    }

    /**
     * Constructs a new UnknownDependencyException with the given message
     *
     * @param message Brief message explaining the cause of the exception
     */
    public UnknownDependencyException(final String message) {
        this(null, message);
    }

    /**
     * Constructs a new UnknownDependencyException based on the given Exception
     *
     * @param message Brief message explaining the cause of the exception
     * @param throwable Exception that triggered this Exception
     */
    public UnknownDependencyException(final Throwable throwable, final String message) {
        this.cause = null;
        this.message = message;
    }

    /**
     * Constructs a new UnknownDependencyException
     */
    public UnknownDependencyException() {
        this(null, "Unknown dependency");
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
