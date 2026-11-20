package io.papermc.paper.plugin.loader.library;

/**
 * Indicates that an exception has occurred while loading a library.
 */
public class LibraryLoadingException extends RuntimeException {

    /**
     * Creates a new library loading exception.
     *
     * @param s the detail message
     */
    public LibraryLoadingException(final String s) {
        super(s);
    }

    /**
     * Creates a new library loading exception with a cause.
     *
     * @param s the detail message
     * @param e the cause
     */
    public LibraryLoadingException(final String s, final Exception e) {
        super(s, e);
    }
}
