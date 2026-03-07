package io.papermc.paper.registry.typed.converter;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import java.util.Optional;
import net.minecraft.util.NullOps;
import net.minecraft.util.Unit;
import org.bukkit.craftbukkit.CraftRegistry;

public record CodecConverter<M, A>(
    Converter<M, A> converter,
    Encoder<M> serializer
) implements Converter<M, A> {

    @Override
    public M toVanilla(final A value) {
        return this.converter.toVanilla(value);
    }

    @Override
    public A fromVanilla(final M value) {
        return this.converter.fromVanilla(value);
    }

    public Optional<String> validate(final M value, final boolean requireRegistryAccess) {
        DynamicOps<Unit> ops = NullOps.INSTANCE;
        if (requireRegistryAccess) {
            ops = CraftRegistry.getMinecraftRegistry().createSerializationContext(ops);
        }
        return this.serializer.encodeStart(ops, value).error().map(DataResult.Error::message);
    }
}
