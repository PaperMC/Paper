package org.bukkit.plugin;

/**
 * Thrown when attempting to load an invalid Plugin file
 */
public class UnknownSoftDependencyException extends UnknownDependencyException {

    private static final long serialVersionUID = 5721389371901775899L;

    /**
     * Constructs a new UnknownSoftDependencyException based on the given Exception
     *
     * @param throwable Exception that triggered this Exception
     */
    public UnknownSoftDependencyException(Throwable throwable) {
        this(throwable, "Unknown soft dependency");
    }

    /**
     * Constructs a new UnknownSoftDependencyException with the given message
     *
     * @param message Brief message explaining the cause of the exception
     */
    public UnknownSoftDependencyException(final String message) {
        this(null, message);
    }

    /**
     * Constructs a new UnknownSoftDependencyException based on the given Exception
     *
     * @param message Brief message explaining the cause of the exception
     * @param throwable Exception that triggered this Exception
     */
    public UnknownSoftDependencyException(final Throwable throwable, final String message) {
        super(throwable, message);
    }

    /**
     * Constructs a new UnknownSoftDependencyException
     */
    public UnknownSoftDependencyException() {
        this(null, "Unknown dependency");
    }
}
