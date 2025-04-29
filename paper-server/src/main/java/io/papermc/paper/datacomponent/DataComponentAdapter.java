package io.papermc.paper.datacomponent;

import java.util.function.Function;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.NullOps;
import net.minecraft.util.Unit;
import org.bukkit.craftbukkit.CraftRegistry;

public record DataComponentAdapter<NMS, API>(
    DataComponentType<NMS> type,
    Function<API, NMS> apiToVanilla,
    Function<NMS, API> vanillaToApi,
    boolean codecValidation
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

    public NMS toVanilla(final API value) {
        final NMS nms = this.apiToVanilla.apply(value);
        if (this.codecValidation) {
            this.type.codecOrThrow().encodeStart(CraftRegistry.getMinecraftRegistry().createSerializationContext(NullOps.INSTANCE), nms).ifError(error -> {
                throw new IllegalArgumentException("Failed to encode data component %s (%s)".formatted(BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(this.type), error.message()));
            });
        }

        return nms;
    }

    public API fromVanilla(final NMS value) {
        return this.vanillaToApi.apply(value);
    }
}
