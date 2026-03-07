package io.papermc.paper.registry.typed;

import io.papermc.paper.registry.typed.converter.Converter;
import io.papermc.paper.registry.typed.converter.ConverterClassDispatcher;
import io.papermc.paper.registry.typed.converter.ConverterDispatcher;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface PaperTypedDataCollector<T> {

    interface Unvalued<T> {
        void registerUnvalued(T type);

        default void registerUnvalued(final Holder.Reference<T> type) {
            this.registerUnvalued(type.key());
        }

        void registerUnvalued(ResourceKey<T> type);
    }

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

    ConverterDispatcher<T> dispatch(Function<T, Converter<?, ?>> converter);

    ConverterClassDispatcher<T> dispatchClass(Function<Type, Converter<?, ?>> converter);

    interface Factory<T, C extends PaperTypedDataCollector<T>> {

        C create(Registry<T> registry, Map<ResourceKey<T>, Converter<?, ?>> adapters);

    }
}
