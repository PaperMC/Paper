package io.papermc.paper.plugin.provider.configuration.serializer.constraints;

import io.papermc.paper.plugin.util.NamespaceChecker;
import org.spongepowered.configurate.objectmapping.meta.Constraint;
import org.spongepowered.configurate.serialize.SerializationException;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

public final class PluginConfigConstraints {

    public static final Set<String> RESERVED_KEYS = Set.of("bukkit", "minecraft", "mojang", "spigot", "paper");

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface PluginName {

        final class Factory implements Constraint.Factory<PluginName, String> {

            private static final Pattern VALID_NAME = Pattern.compile("^[A-Za-z\\d _.-]+$");

            @Override
            public Constraint<String> make(PluginName data, Type type) {
                return value -> {
                    if (value != null) {
                        if (RESERVED_KEYS.contains(value.toLowerCase(Locale.ROOT))) {
                            throw new SerializationException("Restricted name, cannot use '%s' as a plugin name.".formatted(data));
                        } else if (value.indexOf(' ') != -1) {
                            // For legacy reasons, the space condition has a separate exception message.
                            throw new SerializationException("Restricted name, cannot use 0x20 (space character) in a plugin name.");
                        }

                        if (!VALID_NAME.matcher(value).matches()) {
                            throw new SerializationException("name '" + value + "' contains invalid characters.");
                        }
                    }
                };
            }
        }
    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface PluginNameSpace {

        final class Factory implements Constraint.Factory<PluginNameSpace, String> {

            @Override
            public Constraint<String> make(PluginNameSpace data, Type type) {
                return value -> {
                    if (value != null && !NamespaceChecker.isValidNameSpace(value)) {
                        throw new SerializationException("provided class '%s' is in an invalid namespace.".formatted(value));
                    }
                };
            }
        }
    }
}
