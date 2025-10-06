package io.papermc.paper.plugin.loader.library;

/**
 * Indicates that an exception has occurred while loading a library.
 */
public class LibraryLoadingException extends RuntimeException {

    public LibraryLoadingException(String s) {
        super(s);
    }

    public LibraryLoadingException(String s, Exception e) {
        super(s, e);
    }
}
