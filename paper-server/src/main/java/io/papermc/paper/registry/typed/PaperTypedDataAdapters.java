package io.papermc.paper.registry.typed;

import io.papermc.paper.util.converter.Converter;
import io.papermc.paper.util.converter.Converters;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class PaperTypedDataAdapters<T> {

    private final Map<ResourceKey<T>, Converter<?, ?>> converters;

    private PaperTypedDataAdapters(final Map<ResourceKey<T>, Converter<?, ?>> converters) {
        this.converters = converters;
    }

    public static <T, C extends TypedDataCollector<T>> PaperTypedDataAdapters<T> create(
        final Registry<T> registry,
        final TypedDataCollector.Factory<T, C> collectorFactory,
        final Consumer<C> consumer
    ) {
        final Map<ResourceKey<T>, Converter<?, ?>> converters = new IdentityHashMap<>();
        final C collector = collectorFactory.create(registry, converters);

        consumer.accept(collector);

        return new PaperTypedDataAdapters<>(converters);
    }

    @SuppressWarnings("unchecked")
    public <A extends Converter<?, ?>> A get(final ResourceKey<? extends T> key) {
        return (A) this.converters.getOrDefault(key, Converters.unimplemented());
    }
}
