package org.bukkit.configuration.serialization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jetbrains.annotations.NotNull;

/// Represents an "alias" that a [ConfigurationSerializable] may be
/// stored as.
/// If this is not present on a [ConfigurationSerializable] class, it
/// will use the fully qualified name of the class.
///
/// This value will be stored in the configuration so that the configuration
/// deserialization can determine what type it is.
///
/// Using this annotation on any other class than a
/// [ConfigurationSerializable] will have no effect.
///
/// @see ConfigurationSerialization#registerClass(Class, String)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SerializableAs {
    /// This is the name your class will be stored and retrieved as.
    ///
    /// This name MUST be unique. We recommend using names such as
    /// "MyPluginThing" instead of "Thing".
    ///
    /// @return Name to serialize the class as.
    @NotNull
    public String value();
}
