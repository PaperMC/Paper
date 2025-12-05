package io.papermc.paper.registry.data.typed;

import com.mojang.serialization.Codec;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.bukkit.craftbukkit.util.Handleable;
import org.jspecify.annotations.Nullable;

// This class exists only to be implemented, implementations must override the following register methods:
//   void register(final TYPE type, final Function<NMS, API> vanillaToApi, final Function<API, NMS> apiToVanilla)
//   void register(final TYPE type, final Function<NMS, API> vanillaToApi)
// BUT should NOT use the @Override annotation, this is a hack around generics limitations to prevent
// having to define generics on each register call as seen below, making collectors easier to read:
//   collector.<NMSType, APIType>register(...)
public abstract class AbstractTypedDataCollector<TYPE> implements PaperTypedDataCollector<TYPE> {

    private final Registry<TYPE> registry;
    private final Map<ResourceKey<?>, PaperTypedDataAdapter<?, ?>> adapters;

    public AbstractTypedDataCollector(final Registry<TYPE> registry, final Map<ResourceKey<?>, PaperTypedDataAdapter<?, ?>> adapters) {
        this.registry = registry;
        this.adapters = adapters;
    }

    @Override
    public void registerUntyped(final TYPE type) {
        this.registerInternal(this.getKey(type), PaperTypedDataAdapters.UNIT_TO_API_CONVERTER, PaperTypedDataAdapter.API_TO_UNIT_CONVERTER, null);
    }

    @Override
    public <NMS> void registerIdentity(final TYPE type, final Function<TYPE, @Nullable Codec<NMS>> codecGetter) {
        this.registerInternal(this.getKey(type), Function.identity(), Function.identity(), codecGetter.apply(type));
    }

    @Override
    public <NMS, API extends Handleable<NMS>> void register(final TYPE type, final Function<NMS, API> vanillaToApi) {
        this.registerInternal(this.getKey(type), vanillaToApi, Handleable::getHandle, null);
    }

    @Override
    public <NMS, API> void register(final TYPE type, final Function<NMS, API> vanillaToApi, final Function<API, NMS> apiToVanilla) {
        this.registerInternal(this.getKey(type), vanillaToApi, apiToVanilla, null);
    }

    private ResourceKey<TYPE> getKey(final TYPE type) {
        return this.registry.getResourceKey(type).orElseThrow();
    }

    void registerUnimplemented() {
        for (final ResourceKey<TYPE> key : this.registry.registryKeySet()) {
            if (!this.adapters.containsKey(key)) {
                this.registerUnimplemented(key);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void registerUnimplemented(final ResourceKey<TYPE> key) {
        this.registerInternal(key, PaperTypedDataAdapters.UNIMPLEMENTED_TO_API_CONVERTER, PaperTypedDataAdapter.API_TO_UNIMPLEMENTED_CONVERTER, null);
    }

    private <NMS, API> void registerInternal(
        final ResourceKey<?> key,
        final Function<NMS, API> vanillaToApi,
        final Function<API, NMS> apiToVanilla,
        final @Nullable Codec<NMS> codec
    ) {
        if (this.adapters.containsKey(key)) {
            throw new IllegalStateException("Duplicate adapter registration for " + key);
        }

        this.adapters.put(key, new PaperTypedDataAdapter<>(apiToVanilla, vanillaToApi, codec));
    }
}
