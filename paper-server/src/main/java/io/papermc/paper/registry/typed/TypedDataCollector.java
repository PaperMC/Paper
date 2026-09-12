package io.papermc.paper.registry.typed;

import io.papermc.paper.util.converter.Converter;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.Nullable;

public interface TypedDataCollector<T> {

    <M, A> void register(T type, Function<M, A> vanillaToApi, Function<A, M> apiToVanilla);

    default <M, A> void register(final Holder.Reference<T> type, final Function<M, A> vanillaToApi, final Function<A, M> apiToVanilla) {
        this.register(type.key(), vanillaToApi, apiToVanilla);
    }

    <M, A> void register(ResourceKey<T> type, Function<M, A> vanillaToApi, Function<A, M> apiToVanilla);

    <M, A> void register(T type, Converter<M, A> converter);

    default <M, A> void register(final Holder.Reference<T> type, final Converter<M, A> converter) {
        this.register(type.key(), converter);
    }

    <M, A>  void register(ResourceKey<T> type, Converter<M, A> converter);

    Dispatcher<T> dispatch(Function<T, Converter<?, ?>> converter);

    ClassDispatcher<T> dispatchClass(Function<Type, Converter<?, ?>> converter);

    interface Factory<T, C extends TypedDataCollector<T>> {

        C create(Registry<T> registry, Map<ResourceKey<T>, Converter<?, ?>> adapters);
    }

    interface Dispatcher<T> {

        void add(Iterable<T> types);

        void add(T type, T... types);
    }

    interface ClassDispatcher<T> {

        void scanConstants(Class<?> from, ReflectiveFunction<T> toType, BiFunction<T, Field, @Nullable Type> toValueType);

        interface ReflectiveFunction<T> {
            T apply(Field field) throws ReflectiveOperationException;
        }
    }
}
