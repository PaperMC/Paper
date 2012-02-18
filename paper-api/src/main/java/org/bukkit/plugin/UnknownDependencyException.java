package org.bukkit.plugin;

/**
 * Thrown when attempting to load an invalid Plugin file
 */
public class UnknownDependencyException extends RuntimeException {

    private static final long serialVersionUID = 5721389371901775895L;

    /**
     * Constructs a new UnknownDependencyException based on the given Exception
     *
     * @param throwable Exception that triggered this Exception
     */
    public UnknownDependencyException(final Throwable throwable) {
        super(throwable);
    }

    /**
     * Constructs a new UnknownDependencyException with the given message
     *
     * @param message Brief message explaining the cause of the exception
     */
    public UnknownDependencyException(final String message) {
        super(message);
    }

    /**
     * Constructs a new UnknownDependencyException based on the given Exception
     *
     * @param message Brief message explaining the cause of the exception
     * @param throwable Exception that triggered this Exception
     */
    public UnknownDependencyException(final Throwable throwable, final String message) {
        super(message, throwable);
    }

    /**
     * Constructs a new UnknownDependencyException
     */
    public UnknownDependencyException() {

    }
}
