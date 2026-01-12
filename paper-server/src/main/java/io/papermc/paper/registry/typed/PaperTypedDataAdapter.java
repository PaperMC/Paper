package io.papermc.paper.registry.typed;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.function.Function;
import net.minecraft.util.NullOps;
import net.minecraft.util.Unit;
import org.bukkit.craftbukkit.CraftRegistry;
import org.jspecify.annotations.Nullable;

public record PaperTypedDataAdapter<API, NMS>(
    Function<NMS, API> vanillaToApi,
    Function<API, NMS> apiToVanilla,
    @Nullable Codec<NMS> codec
) {
    private static final PaperTypedDataAdapter<?, ?> UNIMPLEMENTED = new PaperTypedDataAdapter<>($ -> {
        throw new UnsupportedOperationException("Cannot convert an unimplemented type to an API value");
    }, $ -> {
        throw new UnsupportedOperationException("Cannot convert an API value to an unimplemented type");
    }, null);

    private static final PaperTypedDataAdapter<?, Unit> UNTYPED = new PaperTypedDataAdapter<>($ -> {
        throw new UnsupportedOperationException("Cannot convert the Unit type to an API value");
    }, $ -> Unit.INSTANCE, null);

    @SuppressWarnings("unchecked")
    public static <API, NMS> PaperTypedDataAdapter<API, NMS> unimplemented() {
        return (PaperTypedDataAdapter<API, NMS>) UNIMPLEMENTED;
    }

    @SuppressWarnings("unchecked")
    public static <API, NMS> PaperTypedDataAdapter<API, NMS> untyped() {
        return (PaperTypedDataAdapter<API, NMS>) UNTYPED;
    }

    public boolean isUntyped() {
        return this == UNTYPED;
    }

    public boolean isUnimplemented() {
        return this == UNIMPLEMENTED;
    }

    public DataResult<NMS> toVanilla(final API value) {
        final NMS nms = this.apiToVanilla.apply(value);
        if (this.codec != null) {
            return this.codec.encodeStart(CraftRegistry.getMinecraftRegistry().createSerializationContext(NullOps.INSTANCE), nms)
                .error().map(error -> DataResult.error(error::message, nms)).orElseGet(() -> DataResult.success(nms));
        }

        return DataResult.success(nms);
    }

    public API fromVanilla(final NMS value) {
        return this.vanillaToApi.apply(value);
    }
}
