package org.bukkit.plugin;

/**
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public class AuthorNagException extends RuntimeException {
    private final String message;

    /**
     * Constructs a new AuthorNagException based on the given Exception
     *
     * @param message Brief message explaining the cause of the exception
     */
    public AuthorNagException(final String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
