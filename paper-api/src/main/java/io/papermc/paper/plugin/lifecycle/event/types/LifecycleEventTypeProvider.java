package io.papermc.paper.plugin.lifecycle.event.types;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventOwner;
import java.util.Optional;
import java.util.ServiceLoader;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
interface LifecycleEventTypeProvider {

    Optional<LifecycleEventTypeProvider> INSTANCE = ServiceLoader.load(LifecycleEventTypeProvider.class, LifecycleEventTypeProvider.class.getClassLoader()).findFirst();

    static LifecycleEventTypeProvider provider() {
        return INSTANCE.orElseThrow();
    }

    <O extends LifecycleEventOwner, E extends LifecycleEvent> LifecycleEventType.Monitorable<O, E> monitor(String name, Class<? extends O> ownerType);

    <O extends LifecycleEventOwner, E extends LifecycleEvent> LifecycleEventType.Prioritizable<O, E> prioritized(String name, Class<? extends O> ownerType);

    TagEventTypeProvider tagProvider();
}
