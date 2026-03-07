package io.papermc.paper.registry.typed.converter;

import com.mojang.serialization.Encoder;
import java.util.function.Function;

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

    default CodecConverter<M, A> serializable(final Encoder<M> serializer) {
        return new CodecConverter<>(this, serializer);
    }

    M toVanilla(A value);

    A fromVanilla(M value);
}
