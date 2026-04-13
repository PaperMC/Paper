package io.papermc.paper.configuration.type.number;

import java.lang.reflect.AnnotatedType;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.apache.commons.lang3.math.NumberUtils;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

public abstract class OptionalNumSerializer<T, O> extends ScalarSerializer.Annotated<T> {

    protected final String emptySerializedValue;
    protected final T emptyValue;
    private final Supplier<O> empty;
    private final Predicate<O> isEmpty;
    private final Function<O, T> factory;
    private final Class<?> number;

    protected OptionalNumSerializer(
        final Class<T> classOfT,
        final String emptySerializedValue,
        final T emptyValue,
        final Supplier<O> empty,
        final Predicate<O> isEmpty,
        final Function<O, T> factory,
        final Class<?> number
    ) {
        super(classOfT);
        this.emptySerializedValue = emptySerializedValue;
        this.emptyValue = emptyValue;
        this.empty = empty;
        this.isEmpty = isEmpty;
        this.factory = factory;
        this.number = number;
    }

    @Override
    public final T deserialize(final AnnotatedType type, final Object obj) throws SerializationException {
        final O value;
        if (obj instanceof final String string) {
            if (this.emptySerializedValue.equalsIgnoreCase(string)) {
                value = this.empty.get();
            } else if (NumberUtils.isParsable(string)) {
                value = this.full(string);
            } else {
                throw new SerializationException("%s (%s) is not a(n) %s or '%s'".formatted(obj, type, this.number.getSimpleName(), this.emptySerializedValue));
            }
        } else if (obj instanceof final Number num) {
            value = this.full(num);
        } else {
            throw new SerializationException("%s (%s) is not a(n) %s or '%s'".formatted(obj, type, this.number.getSimpleName(), this.emptySerializedValue));
        }
        if (this.isEmpty.test(value) || (type.isAnnotationPresent(BelowZeroToEmpty.class) && this.belowZero(value))) {
            return this.emptyValue;
        } else {
            return this.factory.apply(value);
        }
    }

    protected abstract O full(final String value);

    protected abstract O full(final Number num);

    protected abstract boolean belowZero(O value);
}
