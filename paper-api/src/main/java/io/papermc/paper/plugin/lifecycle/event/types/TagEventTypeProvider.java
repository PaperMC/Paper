package io.papermc.paper.plugin.lifecycle.event.types;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.tag.PostFlattenTagRegistrar;
import io.papermc.paper.tag.PreFlattenTagRegistrar;
import org.jetbrains.annotations.ApiStatus;

/**
 * Provides event types for tag registration.
 *
 * @see PreFlattenTagRegistrar
 * @see PostFlattenTagRegistrar
 */
@ApiStatus.NonExtendable
public interface TagEventTypeProvider {

    /**
     * Get a prioritizable, reloadable registrar event for tags before they are flattened.
     *
     * @param registryKey the registry key for the tag type
     * @return the registry event type
     * @param <T> the type of value in the tag
     * @see PreFlattenTagRegistrar
     */
    <T> LifecycleEventType.Prioritizable<BootstrapContext, ReloadableRegistrarEvent<PreFlattenTagRegistrar<T>>> preFlatten(RegistryKey<T> registryKey);

    /**
     * Get a prioritizable, reloadable registrar event for tags after they are flattened.
     *
     * @param registryKey the registry key for the tag type
     * @return the registry event type
     * @param <T> the type of value in the tag
     * @see PostFlattenTagRegistrar
     */
    <T> LifecycleEventType.Prioritizable<BootstrapContext, ReloadableRegistrarEvent<PostFlattenTagRegistrar<T>>> postFlatten(RegistryKey<T> registryKey);
}
