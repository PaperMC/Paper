package com.mojang.serialization.codecs;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;

import java.util.function.Function;
import java.util.stream.Stream;

public class KeyDispatchCodec<K, V> extends MapCodec<V> {
    private final String typeKey;
    private final Codec<K> keyCodec;
    private final String valueKey = "value";
    private final Function<? super V, ? extends DataResult<? extends K>> type;
    private final Function<? super K, ? extends DataResult<? extends Decoder<? extends V>>> decoder;
    private final Function<? super V, ? extends DataResult<? extends Encoder<V>>> encoder;

    private final boolean assumeMap;

    /**
     * Will assume that the result of all elements is a map
     */
    public static <K, V> KeyDispatchCodec<K, V> unsafe(final String typeKey, final Codec<K> keyCodec, final Function<? super V, ? extends DataResult<? extends K>> type, final Function<? super K, ? extends DataResult<? extends Decoder<? extends V>>> decoder, final Function<? super V, ? extends DataResult<? extends Encoder<V>>> encoder) {
        return new KeyDispatchCodec<>(typeKey, keyCodec, type, decoder, encoder, true);
    }

    protected KeyDispatchCodec(final String typeKey, final Codec<K> keyCodec, final Function<? super V, ? extends DataResult<? extends K>> type, final Function<? super K, ? extends DataResult<? extends Decoder<? extends V>>> decoder, final Function<? super V, ? extends DataResult<? extends Encoder<V>>> encoder, final boolean assumeMap) {
        this.typeKey = typeKey;
        this.keyCodec = keyCodec;
        this.type = type;
        this.decoder = decoder;
        this.encoder = encoder;
        this.assumeMap = assumeMap;
    }

    /**
     * Assumes codec(type(V)) is Codec<V>
     */
    public KeyDispatchCodec(final String typeKey, final Codec<K> keyCodec, final Function<? super V, ? extends DataResult<? extends K>> type, final Function<? super K, ? extends DataResult<? extends Codec<? extends V>>> codec) {
        this(typeKey, keyCodec, type, codec, v -> getCodec(type, codec, v), false);
    }

    @Override
    public <T> DataResult<V> decode(final DynamicOps<T> ops, final MapLike<T> input) {
        final T elementName = input.get(typeKey);
        if (elementName == null) {
            return DataResult.error("Input does not contain a key [" + typeKey + "]: " + input);
        }

        return keyCodec.decode(ops, elementName).flatMap(type -> {
            final DataResult<? extends Decoder<? extends V>> elementDecoder = decoder.apply(type.getFirst());
            return elementDecoder.flatMap(c -> {
                if (ops.compressMaps()) {
                    final T value = input.get(ops.createString(valueKey));
                    if (value == null) {
                        return DataResult.error("Input does not have a \"value\" entry: " + input);
                    }
                    return c.parse(ops, value).map(Function.identity());
                }
                if (c instanceof MapCodecCodec<?>) {
                    return ((MapCodecCodec<? extends V>) c).codec().decode(ops, input).map(Function.identity());
                }
                if (assumeMap) {
                    return c.decode(ops, ops.createMap(input.entries())).map(Pair::getFirst);
                }
                return c.decode(ops, input.get(valueKey)).map(Pair::getFirst);
            });
        });
    }

    @Override
    public <T> RecordBuilder<T> encode(final V input, final DynamicOps<T> ops, final RecordBuilder<T> prefix) {
        final DataResult<? extends Encoder<V>> elementEncoder = encoder.apply(input);
        final RecordBuilder<T> builder = prefix.withErrorsFrom(elementEncoder);
        if (!elementEncoder.result().isPresent()) {
            return builder;
        }

        final Encoder<V> c = elementEncoder.result().get();
        if (ops.compressMaps()) {
            return prefix
                .add(typeKey, type.apply(input).flatMap(t -> keyCodec.encodeStart(ops, t)))
                .add(valueKey, c.encodeStart(ops, input));
        }
        if (c instanceof MapCodecCodec<?>) {
            return ((MapCodecCodec<V>) c).codec().encode(input, ops, prefix)
                .add(typeKey, type.apply(input).flatMap(t -> keyCodec.encodeStart(ops, t)));
        }

        final T typeString = ops.createString(typeKey);

        final DataResult<T> result = c.encodeStart(ops, input);
        if (assumeMap) {
            final DataResult<MapLike<T>> element = result.flatMap(ops::getMap);
            return element.map(map -> {
                prefix.add(typeString, type.apply(input).flatMap(t -> keyCodec.encodeStart(ops, t)));
                map.entries().forEach(pair -> {
                    if (!pair.getFirst().equals(typeString)) {
                        prefix.add(pair.getFirst(), pair.getSecond());
                    }
                });
                return prefix;
            }).result().orElseGet(() -> prefix.withErrorsFrom(element));
        }
        prefix.add(typeString, type.apply(input).flatMap(t -> keyCodec.encodeStart(ops, t)));
        prefix.add(valueKey, result);
        return prefix;
    }

    @Override
    public <T> Stream<T> keys(final DynamicOps<T> ops) {
        return Stream.of(typeKey, valueKey).map(ops::createString);
    }

    @SuppressWarnings("unchecked")
    private static <K, V> DataResult<? extends Encoder<V>> getCodec(final Function<? super V, ? extends DataResult<? extends K>> type, final Function<? super K, ? extends DataResult<? extends Encoder<? extends V>>> encoder, final V input) {
        return type.apply(input).<Encoder<? extends V>>flatMap(k -> encoder.apply(k).map(Function.identity())).map(c -> ((Encoder<V>) c));
    }

    @Override
    public String toString() {
        return "KeyDispatchCodec[" + keyCodec.toString() + " " + type + " " + decoder + "]";
    }
}
