package io.papermc.paper.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.DynamicOps;
import io.papermc.paper.adventure.AdventureCodecs;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import java.util.function.Function;
import net.kyori.adventure.key.Key;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryOps;
import org.bukkit.Keyed;
import org.bukkit.Registry;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PaperCodecs {

    public static <A extends Keyed, M> Decoder<A> registryFileDecoderFor(final Decoder<? extends M> directNmsDecoder, final Function<? super Holder<M>, A> directHolderConverter, final RegistryKey<A> registryKey, final boolean allowInline) { // TODO remove Keyed
        final Decoder.Terminal<A> terminalDecoder = directNmsDecoder.map(nms -> directHolderConverter.apply(Holder.direct(nms))).terminal();
        return Decoder.ofTerminal(new Decoder.Terminal<>() {
            @Override
            public <T> DataResult<A> decode(final DynamicOps<T> ops, final T input) {
                // logic based on RegistryFileCodec
                if (ops instanceof RegistryOps<?>) {
                    // Pretty sure we can just use our RegistryAccess here. These codecs aren't ever
                    // used for deserialization, so we don't need to rely on different implementations
                    // of HolderGetter like vanilla's RegistryFileCodec does. We are always just
                    // getting existing dialog elements, never creating empty reference holders.
                    final Registry<A> registry = RegistryAccess.registryAccess().getRegistry(registryKey);
                    final DataResult<Pair<Key, T>> keyDataResult = AdventureCodecs.KEY_CODEC.decode(ops, input);
                    if (keyDataResult.result().isEmpty()) {
                        return !allowInline ? DataResult.error(() -> "Inline definitions not allowed here") : terminalDecoder.decode(ops, input);
                    }
                    final Pair<Key, T> pair = keyDataResult.result().get();
                    final TypedKey<A> elementKey = TypedKey.create(registryKey, pair.getFirst());
                    final A value = registry.get(elementKey);
                    if (value == null) {
                        return DataResult.error(() -> "Failed to get element " + elementKey);
                    }
                    return DataResult.success(value);
                } else {
                    return terminalDecoder.decode(ops, input);
                }
            }
        });
    }

    private PaperCodecs() {
    }
}
