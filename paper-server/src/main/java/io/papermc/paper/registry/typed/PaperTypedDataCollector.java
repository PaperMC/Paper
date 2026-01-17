package io.papermc.paper.registry.typed;

import com.mojang.serialization.Codec;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.Nullable;

public interface PaperTypedDataCollector<TYPE> {

    void registerUntyped(TYPE type);

    default void registerUntyped(Holder.Reference<TYPE> type) {
        this.registerUntyped(type.key());
    }

    void registerUntyped(ResourceKey<TYPE> type);

    <NMS> void registerIdentity(TYPE type, Function<TYPE, @Nullable Codec<NMS>> codecGetter);

    <NMS> void registerIdentity(Holder.Reference<TYPE> type, Function<TYPE, @Nullable Codec<NMS>> codecGetter);

    <NMS> void registerIdentity(ResourceKey<TYPE> type, Function<TYPE, @Nullable Codec<NMS>> codecGetter);

    <NMS, API> void register(TYPE type, Function<NMS, API> vanillaToApi, Function<API, NMS> apiToVanilla);

    default <NMS, API> void register(Holder.Reference<TYPE> type, Function<NMS, API> vanillaToApi, Function<API, NMS> apiToVanilla) {
        this.register(type.key(), vanillaToApi, apiToVanilla);
    }

    <NMS, API> void register(ResourceKey<TYPE> type, Function<NMS, API> vanillaToApi, Function<API, NMS> apiToVanilla);

    interface Factory<TYPE, COLLECTOR extends PaperTypedDataCollector<TYPE>> {

        COLLECTOR create(Registry<TYPE> registry, Map<ResourceKey<TYPE>, PaperTypedDataAdapter<?, ?>> adapters);

    }
}
