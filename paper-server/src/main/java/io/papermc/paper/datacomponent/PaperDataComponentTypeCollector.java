package io.papermc.paper.datacomponent;

import io.papermc.paper.registry.data.typed.AbstractTypedDataCollector;
import io.papermc.paper.registry.data.typed.PaperTypedDataAdapter;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;
import org.bukkit.craftbukkit.util.Handleable;
import java.util.Map;
import java.util.function.Function;

class PaperDataComponentTypeCollector extends AbstractTypedDataCollector<DataComponentType<?>> {

    public PaperDataComponentTypeCollector(final Registry<DataComponentType<?>> registry, final Map<ResourceKey<?>, PaperTypedDataAdapter<?, ?>> adapters) {
        super(registry, adapters);
    }

    // Not using @Override because of generic types
    public <NMS, API extends Handleable<NMS>> void register(final DataComponentType<NMS> dataComponentType, final Function<NMS, API> vanillaToApi) {
        super.register(dataComponentType, vanillaToApi);
    }

    // Not using @Override because of generic types
    public <NMS, API> void register(final DataComponentType<NMS> dataComponentType, final Function<NMS, API> vanillaToApi, final Function<API, NMS> apiToVanilla) {
        super.register(dataComponentType, vanillaToApi, apiToVanilla);
    }
}
