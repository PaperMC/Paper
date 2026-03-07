package io.papermc.paper.registry.typed.converter;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.function.BiFunction;
import org.jspecify.annotations.Nullable;

public interface ConverterClassDispatcher<T> {

    void scanConstants(Class<?> from, ReflectiveFunction<T> toType, BiFunction<T, Field, @Nullable Type> toValueType);

    interface ReflectiveFunction<T> {
        T apply(Field field) throws ReflectiveOperationException;
    }
}
