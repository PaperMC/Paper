package io.papermc.paper.configuration.type.number;

import com.google.common.base.Preconditions;
import com.mojang.logging.LogUtils;
import java.lang.reflect.AnnotatedType;
import java.util.OptionalLong;
import java.util.function.Function;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.spongepowered.configurate.serialize.ScalarSerializer;

public interface LongOr {

    Logger LOGGER = LogUtils.getClassLogger();

    default long or(final long fallback) {
        return this.value().orElse(fallback);
    }

    OptionalLong value();

    default boolean isDefined() {
        return this.value().isPresent();
    }

    default long longValue() {
        return this.value().orElseThrow();
    }

    record Default(OptionalLong value) implements LongOr {
        public static final Default USE_DEFAULT = new Default(OptionalLong.empty());
        private static final String DEFAULT_VALUE = "default";
        public static final ScalarSerializer<Default> SERIALIZER = new Serializer<>(Default.class, Default::new, DEFAULT_VALUE, USE_DEFAULT);
    }

    final class Serializer<T extends LongOr> extends OptionalNumSerializer<T, OptionalLong> {

        private Serializer(final Class<T> classOfT, final Function<OptionalLong, T> factory, final String emptySerializedValue, final T emptyValue) {
            super(classOfT, emptySerializedValue, emptyValue, OptionalLong::empty, OptionalLong::isEmpty, factory, long.class);
        }

        @Override
        protected OptionalLong full(final String value) {
            return OptionalLong.of(Long.parseLong(value));
        }

        @Override
        protected OptionalLong full(final Number num) {
            if (num.longValue() != num.doubleValue()) {
                LOGGER.error("{} cannot be converted to a long without losing information", num);
            }
            return OptionalLong.of(num.longValue());
        }

        @Override
        protected boolean belowZero(final OptionalLong value) {
            Preconditions.checkArgument(value.isPresent());
            return value.getAsLong() < 0;
        }

        @Override
        protected Object serialize(final AnnotatedType type, final T item, final Predicate<Class<?>> typeSupported) {
            final OptionalLong value = item.value();
            if (value.isPresent()) {
                return value.getAsLong();
            } else {
                return this.emptySerializedValue;
            }
        }
    }
}
