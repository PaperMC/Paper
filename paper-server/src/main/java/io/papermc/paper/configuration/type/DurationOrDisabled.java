package io.papermc.paper.configuration.type;

import java.lang.reflect.Type;
import java.util.Optional;
import java.util.function.Predicate;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public final class DurationOrDisabled {
    private static final String DISABLE_VALUE = "disabled";
    public static final DurationOrDisabled USE_DISABLED = new DurationOrDisabled(Optional.empty());
    public static final ScalarSerializer<DurationOrDisabled> SERIALIZER = new Serializer();

    private Optional<Duration> value;

    public DurationOrDisabled(final Optional<Duration> value) {
        this.value = value;
    }

    public Optional<Duration> value() {
        return this.value;
    }

    public void value(final Optional<Duration> value) {
        this.value = value;
    }

    public Duration or(final Duration fallback) {
        return this.value.orElse(fallback);
    }

    private static final class Serializer extends ScalarSerializer<DurationOrDisabled> {
        Serializer() {
            super(DurationOrDisabled.class);
        }

        @Override
        public DurationOrDisabled deserialize(final Type type, final Object obj) throws SerializationException {
            if (obj instanceof final String string) {
                if (DISABLE_VALUE.equalsIgnoreCase(string)) {
                    return USE_DISABLED;
                }
                return new DurationOrDisabled(Optional.of(Duration.SERIALIZER.deserialize(string)));
            }
            throw new SerializationException(obj + "(" + type + ") is not a duration or '" + DISABLE_VALUE + "'");
        }

        @Override
        protected Object serialize(final DurationOrDisabled item, final Predicate<Class<?>> typeSupported) {
            return item.value.map(Duration::value).orElse(DISABLE_VALUE);
        }
    }
}
