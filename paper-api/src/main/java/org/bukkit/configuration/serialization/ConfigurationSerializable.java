package org.bukkit.configuration.serialization;

import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/// Represents an object that may be serialized.
///
/// These objects MUST implement one of the following, in addition to the
/// methods as defined by this interface:
///
///   - A static method "deserialize" that accepts a single [Map]<
///     [String], [Object]> and returns the class.
///   - A static method "valueOf" that accepts a single [Map]<
///     [String], [Object]> and returns the class.
///   - A constructor that accepts a single [Map]<[String],
///     [Object]>.
///
/// In addition to implementing this interface, you must register the class
/// with [ConfigurationSerialization#registerClass(Class)].
///
/// @see DelegateDeserialization
/// @see SerializableAs
public interface ConfigurationSerializable {

    /// Creates a Map representation of this class.
    ///
    /// This class must provide a method to restore this class, as defined in
    /// the [ConfigurationSerializable] interface javadocs.
    /// nb: It is not intended for this method to be called directly, this will
    /// be called by the [ConfigurationSerialization] class.
    ///
    /// @return Map containing the current state of this class
    @NotNull
    @ApiStatus.OverrideOnly
    public Map<String, Object> serialize();
}
