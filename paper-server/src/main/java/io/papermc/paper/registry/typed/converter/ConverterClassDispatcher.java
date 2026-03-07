package io.papermc.paper.registry.typed.converter;

import java.lang.reflect.Field;
import java.util.function.Function;
import org.jspecify.annotations.Nullable;

public interface ConverterClassDispatcher<T> {

    void scanConstants(Class<?> from, ReflectiveFunction<T> toType, Function<Field, @Nullable Class<?>> toValueClass);

    interface ReflectiveFunction<T> {
        T apply(Field field) throws ReflectiveOperationException;
    }
}
