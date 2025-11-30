package io.papermc.paper.registry.data.typed;

import com.mojang.serialization.Codec;
import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.util.NullOps;
import net.minecraft.util.Unit;
import org.bukkit.craftbukkit.CraftRegistry;
import org.jspecify.annotations.Nullable;

public record PaperTypedDataAdapter<API, NMS>(
    Function<API, NMS> apiToVanilla,
    Function<NMS, API> vanillaToApi,
    @Nullable Codec<NMS> codec
) {
    static final Function<Void, Unit> API_TO_UNIT_CONVERTER = $ -> Unit.INSTANCE;

    static final Function API_TO_UNIMPLEMENTED_CONVERTER = $ -> {
        throw new UnsupportedOperationException("Cannot convert an API value to an unimplemented type");
    };

    public boolean isValued() {
        return this.apiToVanilla != API_TO_UNIT_CONVERTER;
    }

    public boolean isUnimplemented() {
        return this.apiToVanilla == API_TO_UNIMPLEMENTED_CONVERTER;
    }

    public NMS toVanilla(final API value, final Holder<?> type) {
        final NMS nms = this.apiToVanilla.apply(value);
        if (this.codec != null) {
            this.codec.encodeStart(CraftRegistry.getMinecraftRegistry().createSerializationContext(NullOps.INSTANCE), nms).ifError(error -> {
                throw new IllegalArgumentException("Failed to encode data component %s (%s)".formatted(type.unwrapKey().orElseThrow(), error.message()));
            });
        }

        return nms;
    }

    public API fromVanilla(final NMS value) {
        return this.vanillaToApi.apply(value);
    }
}
