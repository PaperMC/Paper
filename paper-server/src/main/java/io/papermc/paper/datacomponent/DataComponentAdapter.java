package io.papermc.paper.datacomponent;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.util.Conversions;
import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.RegistryOps;
import net.minecraft.util.NullOps;
import net.minecraft.util.Unit;
import org.jspecify.annotations.Nullable;

public record DataComponentAdapter<NMS, API>(
    Function<API, NMS> apiToVanilla,
    Function<NMS, API> vanillaToApi,
    boolean codecValidation,
    @Nullable HolderValue<NMS, ?> holderValue
) {
    static final Function<Void, Unit> API_TO_UNIT_CONVERTER = _ -> Unit.INSTANCE;

    static final Function API_TO_UNIMPLEMENTED_CONVERTER = _ -> {
        throw new UnsupportedOperationException("Cannot convert an API value to an unimplemented type");
    };

    public boolean isValued() {
        return this.apiToVanilla != API_TO_UNIT_CONVERTER;
    }

    public boolean isUnimplemented() {
        return this.apiToVanilla == API_TO_UNIMPLEMENTED_CONVERTER;
    }

    public NMS toVanilla(final API value, final Holder<? extends DataComponentType<NMS>> type) {
        final NMS nms = this.apiToVanilla.apply(value);
        if (this.codecValidation && !type.value().isTransient()) {
            type.value().codecOrThrow().encodeStart(RegistryOps.create(NullOps.INSTANCE, Conversions.current().lookup()), nms).ifError(error -> {
                throw new IllegalArgumentException("Failed to encode data component %s (%s)".formatted(type.unwrapKey().orElseThrow(), error.message()));
            });
        }

        return nms;
    }

    public API fromVanilla(final NMS value) {
        return this.vanillaToApi.apply(value);
    }

    /**
     * Describes a data component whose entire value is a single holder of a data-driven registry value.
     *
     * @param registryKey the registry of the held value
     * @param unwrap gets the holder from the component value
     * @param wrap creates the component value from a holder
     * @param <NMS> the component value type
     * @param <M> the registry value type
     */
    public record HolderValue<NMS, M>(
        RegistryKey<?> registryKey,
        Function<NMS, Holder<M>> unwrap,
        Function<Holder<M>, NMS> wrap
    ) {
    }
}
