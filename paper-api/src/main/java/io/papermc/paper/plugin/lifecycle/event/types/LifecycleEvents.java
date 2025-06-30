package io.papermc.paper.plugin.lifecycle.event.types;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.datapack.DatapackRegistrar;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventOwner;
import io.papermc.paper.plugin.lifecycle.event.registrar.RegistrarEvent;
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;

/**
 * Holds various types of lifecycle events for
 * use when creating event handler configurations
 * in {@link LifecycleEventManager}.
 */
public final class LifecycleEvents {

    /**
     * This event is for registering commands to the server's brigadier command system. You can register a handler for this event in
     * {@link org.bukkit.plugin.java.JavaPlugin#onEnable()} or {@link io.papermc.paper.plugin.bootstrap.PluginBootstrap#bootstrap(BootstrapContext)}.
     * @see Commands an example of a command being registered
     */
    public static final LifecycleEventType.Prioritizable<LifecycleEventOwner, ReloadableRegistrarEvent<Commands>> COMMANDS = prioritized("commands", LifecycleEventOwner.class);

    /**
     * These events are for registering tags to the server's tag system. You can register a handler for these events
     * only in {@link io.papermc.paper.plugin.bootstrap.PluginBootstrap#bootstrap(BootstrapContext)}.
     */
    @ApiStatus.Experimental
    public static final TagEventTypeProvider TAGS = LifecycleEventTypeProvider.provider().tagProvider();


    /**
     * This event is for informing the server about any available datapacks from other sources such as inside a plugin's jar. You
     * can register a handler for this event only in {@link io.papermc.paper.plugin.bootstrap.PluginBootstrap#bootstrap(BootstrapContext)}.
     * @see DatapackRegistrar an example of a datapack being discovered
     */
    @ApiStatus.Experimental
    public static final LifecycleEventType.Prioritizable<BootstrapContext, RegistrarEvent<DatapackRegistrar>> DATAPACK_DISCOVERY = bootstrapPrioritized("datapack_discovery");

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
