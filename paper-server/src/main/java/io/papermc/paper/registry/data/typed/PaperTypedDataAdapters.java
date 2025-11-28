package io.papermc.paper.registry.data.typed;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Unit;
import org.jspecify.annotations.Nullable;

public final class PaperTypedDataAdapters {

    static final Function<Unit, Void> UNIT_TO_API_CONVERTER = $ -> {
        throw new UnsupportedOperationException("Cannot convert the Unit type to an API value");
    };

    static final Function UNIMPLEMENTED_TO_API_CONVERTER = $ -> {
        throw new UnsupportedOperationException("Cannot convert the an unimplemented type to an API value");
    };

    private final Map<ResourceKey<?>, PaperTypedDataAdapter<?, ?>> adapters;

    private PaperTypedDataAdapters(Map<ResourceKey<?>, PaperTypedDataAdapter<?, ?>> adapters) {
        this.adapters = adapters;
    }

    public static <TYPE, COLLECTOR extends AbstractTypedDataCollector<TYPE>> PaperTypedDataAdapters create(
        Registry<TYPE> registry,
        PaperTypedDataCollector.Factory<TYPE, COLLECTOR> collectorFactory,
        Consumer<COLLECTOR> consumer
    ) {
        Map<ResourceKey<?>, PaperTypedDataAdapter<?, ?>> adapters = new HashMap<>();
        COLLECTOR collector = collectorFactory.create(registry, adapters);

        consumer.accept(collector);

        // Loop through every entry in the registry and register any entry
        // not already registered as unimplemented
        collector.registerUnimplemented();

        return new PaperTypedDataAdapters(adapters);
    }

    @SuppressWarnings("unchecked")
    public <RETURN> @Nullable RETURN getAdapter(ResourceKey<?> key) {
        return (RETURN) this.adapters.get(key);
    }
}
