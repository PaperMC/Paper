package io.papermc.paper.registry.data.typed;

import com.mojang.serialization.Codec;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.bukkit.craftbukkit.util.Handleable;
import org.jspecify.annotations.Nullable;

public interface PaperTypedDataCollector<TYPE> {

    void registerUntyped(final TYPE type);

    <NMS> void registerIdentity(final TYPE type, final Function<TYPE, @Nullable Codec<NMS>> codecGetter);

    <NMS, API extends Handleable<NMS>> void register(final TYPE type, final Function<NMS, API> vanillaToApi);

    <NMS, API> void register(final TYPE type, final Function<NMS, API> vanillaToApi, final Function<API, NMS> apiToVanilla);

    interface Factory<TYPE, COLLECTOR> {

        COLLECTOR create(Registry<TYPE> registry, Map<ResourceKey<?>, PaperTypedDataAdapter<?, ?>> adapters);

    }
}
