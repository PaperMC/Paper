package io.papermc.paper.registry.typed;

import com.mojang.serialization.Codec;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.Nullable;

// This class exists only to be implemented, implementations must override the following register method:
//   void register(final TYPE type, final Function<NMS, API> vanillaToApi, final Function<API, NMS> apiToVanilla)
// BUT should NOT use the @Override annotation, this is a hack around generics limitations to prevent
// having to define generics on each register call as seen below, making collectors easier to read:
//   collector.<NMSType, APIType>register(...)
public abstract class AbstractTypedDataCollector<TYPE> implements PaperTypedDataCollector<TYPE> {

    private final Registry<TYPE> registry;
    private final Map<ResourceKey<TYPE>, PaperTypedDataAdapter<?, ?>> adapters;

    protected AbstractTypedDataCollector(
        final Registry<TYPE> registry,
        final Map<ResourceKey<TYPE>, PaperTypedDataAdapter<?, ?>> adapters
    ) {
        this.registry = registry;
        this.adapters = adapters;
    }

    private ResourceKey<TYPE> getKey(final TYPE type) {
        return this.registry.getResourceKey(type).orElseThrow();
    }

    @Override
    public void registerUntyped(final TYPE type) {
        this.registerUntyped(this.getKey(type));
    }

    @Override
    public void registerUntyped(final ResourceKey<TYPE> type) {
        this.registerInternal(type, PaperTypedDataAdapter.untyped());
    }

    @Override
    public <NMS> void registerIdentity(final TYPE type, final Function<TYPE, @Nullable Codec<NMS>> codecGetter) {
        this.registerInternal(this.getKey(type), Function.identity(), Function.identity(), codecGetter.apply(type));
    }

    @Override
    public <NMS> void registerIdentity(final Holder.Reference<TYPE> type, final Function<TYPE, @Nullable Codec<NMS>> codecGetter) {
        this.registerInternal(type.key(), Function.identity(), Function.identity(), codecGetter.apply(type.value()));
    }

    @Override
    public <NMS> void registerIdentity(final ResourceKey<TYPE> type, final Function<TYPE, @Nullable Codec<NMS>> codecGetter) {
        this.registerInternal(type, Function.identity(), Function.identity(), codecGetter.apply(this.registry.getValueOrThrow(type)));
    }

    @Override
    public <NMS, API> void register(final TYPE type, final Function<NMS, API> vanillaToApi, final Function<API, NMS> apiToVanilla) {
        this.register(this.getKey(type), vanillaToApi, apiToVanilla);
    }

    @Override
    public <NMS, API> void register(final ResourceKey<TYPE> type, final Function<NMS, API> vanillaToApi, final Function<API, NMS> apiToVanilla) {
        this.registerInternal(type, vanillaToApi, apiToVanilla, null);
    }

    private <NMS, API> void registerInternal(
        final ResourceKey<TYPE> key,
        final Function<NMS, API> vanillaToApi,
        final Function<API, NMS> apiToVanilla,
        final @Nullable Codec<NMS> codec
    ) {
        this.registerInternal(key, new PaperTypedDataAdapter<>(vanillaToApi, apiToVanilla, codec));
    }

    private <NMS, API> void registerInternal(
        final ResourceKey<TYPE> key,
        final PaperTypedDataAdapter<NMS, API> adapter
    ) {
        if (this.adapters.put(key, adapter) != null) {
            throw new IllegalStateException("Duplicate adapter registration for " + key);
        }
    }
}
