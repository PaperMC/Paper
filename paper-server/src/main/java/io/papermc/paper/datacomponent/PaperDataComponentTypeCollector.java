package io.papermc.paper.datacomponent;

import io.papermc.paper.registry.typed.AbstractTypedDataCollector;
import io.papermc.paper.registry.typed.PaperTypedDataAdapter;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;
import org.bukkit.craftbukkit.util.Handleable;

class PaperDataComponentTypeCollector extends AbstractTypedDataCollector<DataComponentType<?>> {

    public PaperDataComponentTypeCollector(final Registry<DataComponentType<?>> registry, final Map<ResourceKey<DataComponentType<?>>, PaperTypedDataAdapter<?, ?>> adapters) {
        super(registry, adapters);
    }

    public <NMS> void registerIdentity(final DataComponentType<NMS> dataComponentType) {
        super.registerIdentity(dataComponentType, DataComponentType::codec);
    }

    // Not using @Override because of generic types
    public <NMS, API> void register(final DataComponentType<NMS> dataComponentType, final Function<NMS, API> vanillaToApi, final Function<API, NMS> apiToVanilla) {
        super.register(dataComponentType, vanillaToApi, apiToVanilla);
    }

    public <NMS, API extends Handleable<NMS>> void register(final DataComponentType<NMS> dataComponentType, final Function<NMS, API> vanillaToApi) {
        this.register(dataComponentType, vanillaToApi, Handleable::getHandle);
    }
}
