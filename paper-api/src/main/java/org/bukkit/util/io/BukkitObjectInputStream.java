package org.bukkit.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

/**
 * This class is designed to be used in conjunction with the {@link
 * ConfigurationSerializable} API. It translates objects back to their
 * original implementation after being serialized by {@link
 * BukkitObjectInputStream}.
 * <p>
 * Behavior of implementations extending this class is not guaranteed across
 * future versions.
 */
public class BukkitObjectInputStream extends ObjectInputStream {

    /**
     * Constructor provided to mirror super functionality.
     *
     * @throws IOException if an I/O error occurs while reading stream heade
     * @see ObjectInputStream#ObjectInputStream()
     */
    protected BukkitObjectInputStream() throws IOException, SecurityException {
        super();
        super.enableResolveObject(true);
    }

    /**
     * Object input stream decoration constructor.
     *
     * @param in the input stream to wrap
     * @throws IOException if an I/O error occurs while reading stream header
     * @see ObjectInputStream#ObjectInputStream(InputStream)
     */
    public BukkitObjectInputStream(InputStream in) throws IOException {
        super(in);
        super.enableResolveObject(true);
    }

    @Override
    protected Object resolveObject(Object obj) throws IOException {
        if (obj instanceof Wrapper) {
            try {
                (obj = ConfigurationSerialization.deserializeObject(((Wrapper<?>) obj).map)).getClass(); // NPE
            } catch (Throwable ex) {
                throw newIOException("Failed to deserialize object", ex);
            }
        }

        return super.resolveObject(obj);
    }

    private static IOException newIOException(String string, Throwable cause) {
        IOException exception = new IOException(string);
        exception.initCause(cause);
        return exception;
    }
}
