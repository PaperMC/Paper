package io.papermc.paper.registry.typed;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class PaperTypedDataAdapters<TYPE> {

    private final Map<ResourceKey<TYPE>, PaperTypedDataAdapter<?, ?>> adapters;

    private PaperTypedDataAdapters(final Map<ResourceKey<TYPE>, PaperTypedDataAdapter<?, ?>> adapters) {
        this.adapters = adapters;
    }

    public static <TYPE, COLLECTOR extends AbstractTypedDataCollector<TYPE>> PaperTypedDataAdapters<TYPE> create(
        final Registry<TYPE> registry,
        final PaperTypedDataCollector.Factory<TYPE, COLLECTOR> collectorFactory,
        final Consumer<COLLECTOR> consumer
    ) {
        Map<ResourceKey<TYPE>, PaperTypedDataAdapter<?, ?>> adapters = new IdentityHashMap<>();
        COLLECTOR collector = collectorFactory.create(registry, adapters);

        consumer.accept(collector);

        return new PaperTypedDataAdapters<>(adapters);
    }

    @SuppressWarnings("unchecked")
    public <ADAPTER extends PaperTypedDataAdapter<?, ?>> ADAPTER get(final ResourceKey<? extends TYPE> key) {
        return (ADAPTER) this.adapters.getOrDefault(key, PaperTypedDataAdapter.unimplemented());
    }
}
