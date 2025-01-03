package io.papermc.paper.util;

/**
 * A type of {@link AutoCloseable} that does not throw a checked exception.
 */
public interface SafeAutoClosable extends AutoCloseable {

    @Override
    void close();
}
