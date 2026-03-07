package io.papermc.paper.registry.typed;

import io.papermc.paper.registry.typed.converter.Converter;
import io.papermc.paper.registry.typed.converter.ConverterClassDispatcher;
import io.papermc.paper.registry.typed.converter.ConverterDispatcher;
import io.papermc.paper.registry.typed.converter.Converters;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class TypedDataCollector<T> implements PaperTypedDataCollector<T> {

    private final Registry<T> registry;
    private final Map<ResourceKey<T>, Converter<?, ?>> converters;

    public TypedDataCollector(
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
    public ConverterDispatcher<T> dispatch(final Function<T, Converter<?, ?>> converter) {
        return new ConverterDispatcher<>() {
            @Override
            public void add(final Iterable<T> types) {
                types.forEach(type -> {
                    TypedDataCollector.this.register(TypedDataCollector.this.getKey(type), converter.apply(type));
                });
            }

            @Override
            public void add(final T type, final T... types) {
                TypedDataCollector.this.register(TypedDataCollector.this.getKey(type), converter.apply(type));
                for (final T t : types) {
                    TypedDataCollector.this.register(TypedDataCollector.this.getKey(t), converter.apply(t));
                }
            }
        };
    }

    @Override
    public ConverterClassDispatcher<T> dispatchClass(final Function<Type, Converter<?, ?>> converter) {
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
                TypedDataCollector.this.register(TypedDataCollector.this.getKey(value), converter.apply(valueType));
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
            throw new IllegalStateException("Duplicate adapter registration for " + key);
        }
    }

    public static class Unvaluable<T> extends TypedDataCollector<T> implements PaperTypedDataCollector<T>, Unvalued<T> {
        public Unvaluable(final Registry<T> registry, final Map<ResourceKey<T>, Converter<?, ?>> converters) {
            super(registry, converters);
        }

        @Override
        public void registerUnvalued(final T type) {
            this.registerUnvalued(this.getKey(type));
        }

        @Override
        public void registerUnvalued(final ResourceKey<T> type) {
            this.registerInternal(type, Converters.unvalued());
        }
    }
}
