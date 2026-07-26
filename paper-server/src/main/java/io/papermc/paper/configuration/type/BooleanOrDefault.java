package io.papermc.paper.configuration.type;

import java.lang.reflect.AnnotatedType;
import java.util.Locale;
import java.util.function.Predicate;
import org.apache.commons.lang3.BooleanUtils;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

public record BooleanOrDefault(@Nullable Boolean value) {
    public static final BooleanOrDefault USE_DEFAULT = new BooleanOrDefault(null);
    public static final ScalarSerializer<BooleanOrDefault> SERIALIZER = new Serializer();
    private static final String DEFAULT_VALUE = "default";

    public boolean or(final boolean fallback) {
        return this.value == null ? fallback : this.value;
    }

    private static final class Serializer extends ScalarSerializer.Annotated<BooleanOrDefault> {
        Serializer() {
            super(BooleanOrDefault.class);
        }

        @Override
        public BooleanOrDefault deserialize(final AnnotatedType type, final Object obj) throws SerializationException {
            if (obj instanceof final String string) {
                if (DEFAULT_VALUE.equalsIgnoreCase(string)) {
                    return USE_DEFAULT;
                }
                try {
                    return new BooleanOrDefault(BooleanUtils.toBoolean(string.toLowerCase(Locale.ROOT), "true", "false"));
                } catch (final IllegalArgumentException ex) {
                    throw new SerializationException(BooleanOrDefault.class, obj + "(" + type + ") is not a boolean or '" + DEFAULT_VALUE + "'", ex);
                }
            } else if (obj instanceof final Boolean bool) {
                return new BooleanOrDefault(bool);
            }
            throw new SerializationException(BooleanOrDefault.class, obj + "(" + type + ") is not a boolean or '" + DEFAULT_VALUE + "'");
        }

        @Override
        protected Object serialize(final AnnotatedType type, final BooleanOrDefault item, final Predicate<Class<?>> typeSupported) {
            final Boolean value = item.value;
            if (value != null) {
                return value.toString();
            } else {
                return DEFAULT_VALUE;
            }
        }
    }
}
