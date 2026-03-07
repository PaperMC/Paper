package io.papermc.paper.registry.typed.converter;

import com.mojang.serialization.Encoder;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.papermc.paper.util.MCUtil.transformUnmodifiable;

public interface Converter<M, A> {

    static <M, A> Converter<M, A> direct(final Function<M, A> vanillaToApi, final Function<A, M> apiToVanilla) {
        return new Converter<>() {
            @Override
            public M toVanilla(final A value) {
                return apiToVanilla.apply(value);
            }

            @Override
            public A fromVanilla(final M value) {
                return vanillaToApi.apply(value);
            }
        };
    }

    static <I> Converter<I, I> identity() {
        final class Holder {
            private static final Converter<?, ?> NO_OP = direct(Function.identity(), Function.identity());
        }

        //noinspection unchecked
        return (Converter<I, I>) Holder.NO_OP;
    }

    static <I> Converter<I, I> identity(final Encoder<I> serializer) {
        return new CodecConverter<>(identity(), serializer);
    }

    default Converter<M, A> serializable(final Encoder<M> serializer) {
        return new CodecConverter<>(this, serializer);
    }

    default Converter<Collection<? extends M>, Collection<? extends A>> collection() {
        if (this instanceof CodecConverter<M, A> codecConverter) {
            return codecConverter.converter().collection();
        }
        return Converter.direct(
            collection -> transformUnmodifiable(collection, this::fromVanilla),
            collection -> transformUnmodifiable(collection, this::toVanilla)
        );
    }

    default Converter<List<? extends M>, List<? extends A>> list() {
        if (this instanceof CodecConverter<M, A> codecConverter) {
            return codecConverter.converter().list();
        }
        return Converter.direct(
            collection -> transformUnmodifiable(collection, this::fromVanilla),
            collection -> transformUnmodifiable(collection, this::toVanilla)
        );
    }

    default Converter<Set<? extends M>, Set<? extends A>> set() {
        if (this instanceof CodecConverter<M, A> codecConverter) {
            return codecConverter.converter().set();
        }
        return Converter.direct(
            set -> set.stream().map(this::fromVanilla).collect(Collectors.toUnmodifiableSet()),
            set -> set.stream().map(this::toVanilla).collect(Collectors.toUnmodifiableSet())
        );
    }

    M toVanilla(A value);

    A fromVanilla(M value);
}
