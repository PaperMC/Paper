package org.bukkit.util.io;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

/**
 * This class is designed to be used in conjunction with the {@link
 * ConfigurationSerializable} API. It translates objects to an internal
 * implementation for later deserialization using {@link
 * BukkitObjectInputStream}.
 * <p>
 * Behavior of implementations extending this class is not guaranteed across
 * future versions.
 */
public class BukkitObjectOutputStream extends ObjectOutputStream {

    /**
     * Constructor provided to mirror super functionality.
     *
     * @throws IOException if an I/O error occurs while creating this stream
     * @throws SecurityException if a security manager exists and denies
     * enabling subclassing
     * @see ObjectOutputStream#ObjectOutputStream()
     */
    protected BukkitObjectOutputStream() throws IOException, SecurityException {
        super();
        super.enableReplaceObject(true);
    }

    /**
     * Object output stream decoration constructor.
     *
     * @param out the stream to wrap
     * @throws IOException if an I/O error occurs while writing stream header
     * @see ObjectOutputStream#ObjectOutputStream(OutputStream)
     */
    public BukkitObjectOutputStream(OutputStream out) throws IOException {
        super(out);
        super.enableReplaceObject(true);
    }

    @Override
    protected Object replaceObject(Object obj) throws IOException {
        if (!(obj instanceof Serializable) && (obj instanceof ConfigurationSerializable)) {
            obj = Wrapper.newWrapper((ConfigurationSerializable) obj);
        }

        return super.replaceObject(obj);
    }
}
