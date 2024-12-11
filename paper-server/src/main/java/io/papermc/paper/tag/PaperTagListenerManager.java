package io.papermc.paper.tag;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventRunner;
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent;
import io.papermc.paper.plugin.lifecycle.event.types.AbstractLifecycleEventType;
import io.papermc.paper.plugin.lifecycle.event.types.PrioritizableLifecycleEventType;
import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.event.RegistryEventMap;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagLoader;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public class PaperTagListenerManager {

    public static final String PRE_FLATTEN_EVENT_NAME = "pre-flatten";
    public static final String POST_FLATTEN_EVENT_NAME = "post-flatten";

    public static final PaperTagListenerManager INSTANCE = new PaperTagListenerManager();

    public final RegistryEventMap preFlatten = new RegistryEventMap(PRE_FLATTEN_EVENT_NAME);
    public final RegistryEventMap postFlatten = new RegistryEventMap(POST_FLATTEN_EVENT_NAME);

    private PaperTagListenerManager() {
    }

    public <A> Map<ResourceLocation, List<TagLoader.EntryWithSource>> firePreFlattenEvent(
        final Map<ResourceLocation, List<TagLoader.EntryWithSource>> initial,
        final @Nullable TagEventConfig<?, A> config
    ) {
        if (config == null || config.preFlatten() == null || !config.preFlatten().hasHandlers()) {
            return initial;
        }
        final PaperPreFlattenTagRegistrar<A> registrar = new PaperPreFlattenTagRegistrar<>(initial, config);
        LifecycleEventRunner.INSTANCE.callReloadableRegistrarEvent(
            config.preFlatten(),
            registrar,
            BootstrapContext.class,
            config.cause()
        );
        return Map.copyOf(registrar.tags);
    }

    public <M, A> Map<ResourceLocation, List<M>> firePostFlattenEvent(
        final Map<ResourceLocation, List<M>> initial,
        final @Nullable TagEventConfig<M, A> config
    ) {
        if (config == null || config.postFlatten() == null || !config.postFlatten().hasHandlers()) {
            return initial;
        }
        final PaperPostFlattenTagRegistrar<M, A> registrar = new PaperPostFlattenTagRegistrar<>(initial, config);
        LifecycleEventRunner.INSTANCE.callReloadableRegistrarEvent(
            config.postFlatten(),
            registrar,
            BootstrapContext.class,
            config.cause()
        );
        return Map.copyOf(registrar.tags);
    }

    public <M, B> @Nullable TagEventConfig<Holder<M>, B> createEventConfig(final Registry<M> registry, final ReloadableRegistrarEvent.Cause cause) {
        if (PaperRegistries.getEntry(registry.key()) == null) {
            // TODO probably should be able to modify every registry
            return null;
        }
        final RegistryKey<B> registryKey = PaperRegistries.registryFromNms(registry.key());
        @Nullable AbstractLifecycleEventType<BootstrapContext, ReloadableRegistrarEvent<PreFlattenTagRegistrar<B>>, ?> preFlatten = null;
        if (this.preFlatten.hasHandlers(registryKey)) {
            preFlatten = (AbstractLifecycleEventType<BootstrapContext, ReloadableRegistrarEvent<PreFlattenTagRegistrar<B>>, ?>) this.preFlatten.<B, ReloadableRegistrarEvent<PreFlattenTagRegistrar<B>>>getEventType(registryKey);
        }
        @Nullable AbstractLifecycleEventType<BootstrapContext, ReloadableRegistrarEvent<PostFlattenTagRegistrar<B>>, ?> postFlatten = null;
        if (this.postFlatten.hasHandlers(registryKey)) {
            postFlatten = (AbstractLifecycleEventType<BootstrapContext, ReloadableRegistrarEvent<PostFlattenTagRegistrar<B>>, ?>) this.postFlatten.<B, ReloadableRegistrarEvent<PostFlattenTagRegistrar<B>>>getEventType(registryKey);
        }
        return new TagEventConfig<>(
            preFlatten,
            postFlatten,
            cause,
            registry::get,
            h -> ((Holder.Reference<M>) h).key().location(),
            PaperRegistries.registryFromNms(registry.key())
        );
    }

    public <T> PrioritizableLifecycleEventType.Simple<BootstrapContext, ReloadableRegistrarEvent<PreFlattenTagRegistrar<T>>> getPreFlattenType(final RegistryKey<T> registryKey) {
        return this.preFlatten.getOrCreate(registryKey, (ignored, name) -> {
            return new PrioritizableLifecycleEventType.Simple<>(name, BootstrapContext.class);
        });
    }

    public <T> PrioritizableLifecycleEventType.Simple<BootstrapContext, ReloadableRegistrarEvent<PostFlattenTagRegistrar<T>>> getPostFlattenType(final RegistryKey<T> registryKey) {
        return this.postFlatten.getOrCreate(registryKey, (ignored, name) -> {
            return new PrioritizableLifecycleEventType.Simple<>(name, BootstrapContext.class);
        });
    }
}
