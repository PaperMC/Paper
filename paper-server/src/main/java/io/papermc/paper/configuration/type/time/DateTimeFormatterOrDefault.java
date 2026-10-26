package io.papermc.paper.configuration.type.time;

import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;
import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class DateTimeFormatterOrDefault {
    public static final Serializer SERIALIZER = new Serializer();
    public static final DateTimeFormatterOrDefault USE_DEFAULT = new DateTimeFormatterOrDefault(Optional.empty(), Serializer.DEFAULT_VALUE);

    private final Optional<DateTimeFormatter> dateTimeFormatter;
    private final String value;

    public boolean isDefined() {
        return this.dateTimeFormatter.isPresent();
    }

    public DateTimeFormatter or(final DateTimeFormatter def) {
        return this.dateTimeFormatter.orElse(def);
    }

    public DateTimeFormatter orElseThrow() throws NoSuchElementException {
        return this.dateTimeFormatter.orElseThrow();
    }

    private DateTimeFormatterOrDefault(final Optional<DateTimeFormatter> dateTimeFormatter, final String value) {
        this.dateTimeFormatter = dateTimeFormatter;
        this.value = value;
    }

    public static final class Serializer implements TypeSerializer<DateTimeFormatterOrDefault> {
        private static final String DEFAULT_VALUE = "default";

        @Override
        public DateTimeFormatterOrDefault deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
            final String value = node.getString(DEFAULT_VALUE);

            if (value.equals(DEFAULT_VALUE)) {
                return new DateTimeFormatterOrDefault(Optional.empty(), DEFAULT_VALUE);
            }

            try {
                return new DateTimeFormatterOrDefault(Optional.of(DateTimeFormatter.ofPattern(value, Locale.ROOT)), value);
            } catch (IllegalArgumentException e) {
                throw new SerializationException(DateTimeFormatter.class, "Failed to parse pattern '" + value + "' to a valid DateTimeFormatter format", e);
            }
        }

        @Override
        public void serialize(final Type type, @Nullable final DateTimeFormatterOrDefault obj, final ConfigurationNode node) throws SerializationException {
            node.set(obj == null ? DEFAULT_VALUE : obj.value);
        }
    }
}
