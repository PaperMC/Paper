package io.papermc.paper.registry.typed;

import io.papermc.paper.util.converter.Converter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class PaperTypedDataCollector<T> implements TypedDataCollector<T> {

    private final Registry<T> registry;
    private final Map<ResourceKey<T>, Converter<?, ?>> converters;

    public PaperTypedDataCollector(
        final Registry<T> registry,
        final Map<ResourceKey<T>, Converter<?, ?>> converters
    ) {
        this.registry = registry;
        this.converters = converters;
    }

    protected ResourceKey<T> getKey(final T type) {
        return this.registry.getResourceKey(type).orElseThrow();
    }

    @Override
    public <M, A> void register(final T type, final Function<M, A> vanillaToApi, final Function<A, M> apiToVanilla) {
        this.register(this.getKey(type), vanillaToApi, apiToVanilla);
    }

    @Override
    public <M, A> void register(final ResourceKey<T> type, final Function<M, A> vanillaToApi, final Function<A, M> apiToVanilla) {
        this.registerInternal(type, vanillaToApi, apiToVanilla);
    }

    @Override
    public <M, A> void register(final T type, final Converter<M, A> converter) {
        this.register(this.getKey(type), converter);
    }

    @Override
    public <M, A> void register(final ResourceKey<T> type, final Converter<M, A> converter) {
        this.registerInternal(type, converter);
    }

    @Override
    public Dispatcher<T> dispatch(final Function<T, Converter<?, ?>> converter) {
        return new Dispatcher<>() {
            @Override
            public void add(final Iterable<T> types) {
                types.forEach(type -> {
                    PaperTypedDataCollector.this.register(PaperTypedDataCollector.this.getKey(type), converter.apply(type));
                });
            }

            @Override
            public void add(final T type, final T... types) {
                PaperTypedDataCollector.this.register(PaperTypedDataCollector.this.getKey(type), converter.apply(type));
                for (final T t : types) {
                    PaperTypedDataCollector.this.register(PaperTypedDataCollector.this.getKey(t), converter.apply(t));
                }
            }
        };
    }

    @Override
    public ClassDispatcher<T> dispatchClass(final Function<Type, Converter<?, ?>> converter) {
        return (from, toType, toValueType) -> {
            for (final Field field : from.getDeclaredFields()) {
                final int mod = field.getModifiers();
                if (!Modifier.isStatic(mod) || !Modifier.isPublic(mod)) {
                    continue;
                }

                final T value;
                try {
                    value = toType.apply(field);
                } catch (final ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
                final Type valueType = toValueType.apply(value, field);
                if (valueType == null) {
                    continue;
                }
                PaperTypedDataCollector.this.register(PaperTypedDataCollector.this.getKey(value), converter.apply(valueType));
            }
        };
    }

    protected <M, A> void registerInternal(
        final ResourceKey<T> key,
        final Function<M, A> vanillaToApi,
        final Function<A, M> apiToVanilla
    ) {
        this.registerInternal(key, Converter.direct(vanillaToApi, apiToVanilla));
    }

    protected <M, A> void registerInternal(
        final ResourceKey<T> key,
        final Converter<M, A> converter
    ) {
        if (this.converters.put(key, converter) != null) {
            throw new IllegalStateException("Duplicate converter registration for " + key);
        }
    }
}
