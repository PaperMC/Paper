package org.bukkit.configuration.serialization;

import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an object that may be serialized.
 * <p>
 * These objects MUST implement one of the following, in addition to the
 * methods as defined by this interface:
 * <ul>
 * <li>A static method "deserialize" that accepts a single {@link Map}&lt;
 * {@link String}, {@link Object}&gt; and returns the class.</li>
 * <li>A static method "valueOf" that accepts a single {@link Map}&lt;{@link
 * String}, {@link Object}&gt; and returns the class.</li>
 * <li>A constructor that accepts a single {@link Map}&lt;{@link String},
 * {@link Object}&gt;.</li>
 * </ul>
 * In addition to implementing this interface, you must register the class
 * with {@link ConfigurationSerialization#registerClass(Class)}.
 *
 * @see DelegateDeserialization
 * @see SerializableAs
 */
public interface ConfigurationSerializable {

    /**
     * Creates a Map representation of this class.
     * <p>
     * This class must provide a method to restore this class, as defined in
     * the {@link ConfigurationSerializable} interface javadocs.
     *
     * nb: It is not intended for this method to be called directly, this will
     * be called by the {@link ConfigurationSerialization} class.
     *
     * @return Map containing the current state of this class
     */
    @NotNull
    @ApiStatus.OverrideOnly
    public Map<String, Object> serialize();
}
