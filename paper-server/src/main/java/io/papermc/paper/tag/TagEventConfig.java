package io.papermc.paper.tag;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent;
import io.papermc.paper.plugin.lifecycle.event.types.AbstractLifecycleEventType;
import io.papermc.paper.registry.RegistryKey;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public record TagEventConfig<M, A>(
    @Nullable AbstractLifecycleEventType<BootstrapContext, ? extends ReloadableRegistrarEvent<PreFlattenTagRegistrar<A>>, ?> preFlatten,
    @Nullable AbstractLifecycleEventType<BootstrapContext, ? extends ReloadableRegistrarEvent<PostFlattenTagRegistrar<A>>, ?> postFlatten,
    ReloadableRegistrarEvent.Cause cause,
    Function<ResourceLocation, Optional<? extends M>> fromIdConverter,
    Function<M, ResourceLocation> toIdConverter,
    RegistryKey<A> apiRegistryKey
) {
}
