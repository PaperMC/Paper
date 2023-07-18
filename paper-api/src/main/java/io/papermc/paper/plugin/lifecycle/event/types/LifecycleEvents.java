package io.papermc.paper.plugin.lifecycle.event.types;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventOwner;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Holds various types of lifecycle events for
 * use when creating event handler configurations
 * in {@link LifecycleEventManager}.
 */
@ApiStatus.Experimental
@NullMarked
public final class LifecycleEvents {

    //<editor-fold desc="helper methods" defaultstate="collapsed">
    @ApiStatus.Internal
    static <E extends LifecycleEvent> LifecycleEventType.Monitorable<Plugin, E> plugin(final String name) {
        return monitor(name, Plugin.class);
    }

    @ApiStatus.Internal
    static <E extends LifecycleEvent> LifecycleEventType.Prioritizable<Plugin, E> pluginPrioritized(final String name) {
        return prioritized(name, Plugin.class);
    }

    @ApiStatus.Internal
    static <E extends LifecycleEvent> LifecycleEventType.Monitorable<BootstrapContext, E> bootstrap(final String name) {
        return monitor(name, BootstrapContext.class);
    }

    @ApiStatus.Internal
    static <E extends LifecycleEvent> LifecycleEventType.Prioritizable<BootstrapContext, E> bootstrapPrioritized(final String name) {
        return prioritized(name, BootstrapContext.class);
    }

    @ApiStatus.Internal
    static <O extends LifecycleEventOwner, E extends LifecycleEvent, O2 extends O> LifecycleEventType.Monitorable<O, E> monitor(final String name, final Class<O2> ownerType) {
        return LifecycleEventTypeProvider.provider().monitor(name, ownerType);
    }

    @ApiStatus.Internal
    static <O extends LifecycleEventOwner, E extends LifecycleEvent> LifecycleEventType.Prioritizable<O, E> prioritized(final String name, final Class<? extends O> ownerType) {
        return LifecycleEventTypeProvider.provider().prioritized(name, ownerType);
    }
    //</editor-fold>

    private LifecycleEvents() {
    }
}
