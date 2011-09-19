package org.bukkit.configuration.serialization;

import java.util.Map;

/**
 * Represents an object that may be serialized.
 * <p>
 * These objects MUST implement one of the following, in addition to the methods
 * as defined by this interface:
 * - A static method "deserialize" that accepts a single {@link Map<String, Object>} and returns the class.
 * - A static method "valueOf" that accepts a single {@link Map<String, Object>} and returns the class.
 * - A constructor that accepts a single {@link Map<String, Object>}.
 */
public interface ConfigurationSerializable {
    /**
     * Creates a Map representation of this class.
     * <p>
     * This class must provide a method to restore this class, as defined in the
     * {@link ConfigurationSerializable} interface javadocs.
     * 
     * @return Map containing the current state of this class
     */
    public Map<String, Object> serialize();
}
