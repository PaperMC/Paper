package io.papermc.testplugin;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.dummy.DummyResourceRegistrar;
import io.papermc.paper.plugin.lifecycle.dummy.NonRegistrarEvent;
import io.papermc.paper.plugin.lifecycle.dummy.RegisterAnywhereEvent;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventOwner;
import io.papermc.paper.plugin.lifecycle.event.handler.LifecycleEventHandler;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEvents;
import io.papermc.paper.plugin.lifecycle.event.handler.configuration.MonitorLifecycleEventHandlerConfiguration;
import io.papermc.paper.plugin.lifecycle.event.registrar.RegistrarEvent;
import org.jetbrains.annotations.NotNull;

public class TestPluginBootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {

        final LifecycleEventManager<BootstrapContext> lifecycles = context.getLifecycleManager();
        lifecycles.registerEventHandler(LifecycleEvents.DUMMY.handler(event -> {
            final DummyResourceRegistrar registrar = event.registrar();
            final RegistrarEvent.Reloadable.Cause cause = event.cause();
            System.out.println("dummy hook: " + cause);
        }));

        lifecycles.registerEventHandler(LifecycleEvents.DUMMY.handler(this.handle("dummy FIRST")));
        lifecycles.registerEventHandler(LifecycleEvents.DUMMY.handler(this.handle("dummy LAST (monitor)")).monitor());
        lifecycles.registerEventHandler(LifecycleEvents.DUMMY.handler(this.handle("dummy SECOND")));


        final MonitorLifecycleEventHandlerConfiguration<LifecycleEventOwner> handler = LifecycleEvents.REGISTER_ANYWHERE_EVENT.handler(event -> {

        });
        // lifecycles.registerEventHandler(LifecycleEvents.DUMMY_STATIC.handler(event -> { // shouldn't compile
        //
        // }));
        lifecycles.registerEventHandler(handler);
        lifecycles.registerEventHandler(LifecycleEvents.NON_REGISTRAR_RELATED_EVENT.handler(NonRegistrarEvent::someNonRegistrarRelatedThing));
    }

    private LifecycleEventHandler<RegistrarEvent<DummyResourceRegistrar>> handle(final String out) {
        return event -> {
            System.out.println(out);
        };
    }

}
