package io.papermc.paper.configuration.constraint;

import io.papermc.paper.configuration.type.number.DoubleOr;
import io.papermc.paper.configuration.type.number.IntOr;
import io.papermc.paper.configuration.type.number.LongOr;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.Function;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.meta.Constraint;
import org.spongepowered.configurate.serialize.SerializationException;

import static io.leangen.geantyref.GenericTypeReflector.box;
import static io.leangen.geantyref.GenericTypeReflector.erase;

public final class Constraints {
    private Constraints() {
    }

    public static final class Positive implements Constraint<Number> {
        @Override
        public void validate(final @Nullable Number value) throws SerializationException {
            if (value != null && value.doubleValue() <= 0) {
                throw new SerializationException(value + " should be positive");
            }
        }
    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Min {
        double value();

        final class Factory implements Constraint.Factory<Min, Object> {
            @Override
            public Constraint<Object> make(final Min data, final Type type) {
                final Function<Object, OptionalDouble> extractor = numberExtractor(type);
                return value -> {
                    if (value == null) {
                        return;
                    }
                    final OptionalDouble number = extractor.apply(value);
                    if (number.isPresent() && number.getAsDouble() < data.value()) {
                        throw new SerializationException(format(number.getAsDouble()) + " is less than the min " + format(data.value()));
                    }
                };
            }
        }
    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Max {
        double value();

        final class Factory implements Constraint.Factory<Max, Object> {
            @Override
            public Constraint<Object> make(final Max data, final Type type) {
                final Function<Object, OptionalDouble> extractor = numberExtractor(type);
                return value -> {
                    if (value == null) {
                        return;
                    }
                    final OptionalDouble number = extractor.apply(value);
                    if (number.isPresent() && number.getAsDouble() > data.value()) {
                        throw new SerializationException(format(number.getAsDouble()) + " is greater than the max " + format(data.value()));
                    }
                };
            }
        }
    }

    // empty for the "default"/"disabled" values of the NumberOr types, which are not range checked
    private static Function<Object, OptionalDouble> numberExtractor(final Type type) {
        final Class<?> erased = erase(box(type));
        if (Number.class.isAssignableFrom(erased)) {
            return value -> OptionalDouble.of(((Number) value).doubleValue());
        } else if (IntOr.class.isAssignableFrom(erased)) {
            return value -> {
                final OptionalInt number = ((IntOr) value).value();
                return number.isPresent() ? OptionalDouble.of(number.getAsInt()) : OptionalDouble.empty();
            };
        } else if (LongOr.class.isAssignableFrom(erased)) {
            return value -> {
                final OptionalLong number = ((LongOr) value).value();
                return number.isPresent() ? OptionalDouble.of(number.getAsLong()) : OptionalDouble.empty();
            };
        } else if (DoubleOr.class.isAssignableFrom(erased)) {
            return value -> ((DoubleOr) value).value();
        }
        throw new IllegalArgumentException(type.getTypeName() + " is not a number type that supports range constraints");
    }

    private static String format(final double value) {
        return BigDecimal.valueOf(value).stripTrailingZeros().toPlainString();
    }
}
