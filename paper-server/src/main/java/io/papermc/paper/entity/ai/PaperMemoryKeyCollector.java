package io.papermc.paper.entity.ai;

import io.papermc.paper.registry.typed.AbstractTypedDataCollector;
import io.papermc.paper.registry.typed.PaperTypedDataAdapter;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import org.bukkit.craftbukkit.util.Handleable;

class PaperMemoryKeyCollector extends AbstractTypedDataCollector<MemoryModuleType<?>> {

    public PaperMemoryKeyCollector(final Registry<MemoryModuleType<?>> registry, final Map<ResourceKey<MemoryModuleType<?>>, PaperTypedDataAdapter<?, ?>> adapters) {
        super(registry, adapters);
    }

    public <NMS> void registerIdentity(final MemoryModuleType<NMS> moduleType) {
        super.registerIdentity(moduleType, module -> null); // todo access underlying codec if needed
    }

    // Not using @Override because of generic types
    public <NMS, API> void register(final MemoryModuleType<NMS> moduleType, final Function<NMS, API> vanillaToApi, final Function<API, NMS> apiToVanilla) {
        super.register(moduleType, vanillaToApi, apiToVanilla);
    }

    public <NMS, API extends Handleable<NMS>> void register(final MemoryModuleType<NMS> moduleType, final Function<NMS, API> vanillaToApi) {
        this.register(moduleType, vanillaToApi, Handleable::getHandle);
    }
}
