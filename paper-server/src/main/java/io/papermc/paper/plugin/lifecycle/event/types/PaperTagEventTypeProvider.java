package io.papermc.paper.plugin.lifecycle.event.types;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.tag.PaperTagListenerManager;
import io.papermc.paper.tag.PostFlattenTagRegistrar;
import io.papermc.paper.tag.PreFlattenTagRegistrar;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public class PaperTagEventTypeProvider implements TagEventTypeProvider {

    @Override
    public <T> PrioritizableLifecycleEventType.Simple<BootstrapContext, ReloadableRegistrarEvent<PreFlattenTagRegistrar<T>>> preFlatten(final RegistryKey<T> registryKey) {
        return PaperTagListenerManager.INSTANCE.getPreFlattenType(registryKey);
    }

    @Override
    public <T> PrioritizableLifecycleEventType.Simple<BootstrapContext, ReloadableRegistrarEvent<PostFlattenTagRegistrar<T>>> postFlatten(final RegistryKey<T> registryKey) {
        return PaperTagListenerManager.INSTANCE.getPostFlattenType(registryKey);
    }
}
