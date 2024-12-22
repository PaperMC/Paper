package io.papermc.paper.plugin.loader.library;

/**
 * Indicates that an exception has occured while loading a library.
 *
 * @since 1.19.3
 */
public class LibraryLoadingException extends RuntimeException {

    public LibraryLoadingException(String s) {
        super(s);
    }

    public LibraryLoadingException(String s, Exception e) {
        super(s, e);
    }
}
