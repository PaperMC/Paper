package io.papermc.paper.world.attribute;

import io.papermc.paper.registry.data.typed.AbstractTypedDataCollector;
import io.papermc.paper.registry.data.typed.PaperTypedDataAdapter;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.attribute.EnvironmentAttribute;
import org.bukkit.craftbukkit.util.Handleable;

class PaperEnvironmentalAttributeTypeCollector extends AbstractTypedDataCollector<EnvironmentAttribute<?>> {

    public PaperEnvironmentalAttributeTypeCollector(final Registry<EnvironmentAttribute<?>> registry, final Map<ResourceKey<?>, PaperTypedDataAdapter<?, ?>> adapters) {
        super(registry, adapters);
    }

    // Not using @Override because of generic types
    public <NMS, API extends Handleable<NMS>> void register(final EnvironmentAttribute<NMS> dataComponentType, final Function<NMS, API> vanillaToApi) {
        super.register(dataComponentType, vanillaToApi);
    }

    // Not using @Override because of generic types
    public <NMS, API> void register(final EnvironmentAttribute<NMS> dataComponentType, final Function<NMS, API> vanillaToApi, final Function<API, NMS> apiToVanilla) {
        super.register(dataComponentType, vanillaToApi, apiToVanilla);
    }
}
