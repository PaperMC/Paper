package io.papermc.testplugin;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.dummy.DummyResourceRegistrar;
import io.papermc.paper.plugin.lifecycle.dummy.NonRegistrarEvent;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventHandler;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEvents;
import io.papermc.paper.plugin.lifecycle.event.registrar.RegistrarEvent;
import org.jetbrains.annotations.NotNull;

public class TestPluginBootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {

        final LifecycleEventManager<BootstrapContext> lifecycles = context.getLifecycleManager();
        lifecycles.registerEventHandler(LifecycleEvents.DUMMY, event -> {
            final DummyResourceRegistrar registrar = event.registrar();
            final RegistrarEvent.Reloadable.Cause cause = event.cause();
            System.out.println("dummy hook: " + cause);
        });

        lifecycles.registerEventHandler(LifecycleEvents.DUMMY, this.handle("dummy FIRST"));
        lifecycles.registerMonitorEventHandler(LifecycleEvents.DUMMY, this.handle("dummy LAST (monitor)"));
        lifecycles.registerEventHandler(LifecycleEvents.DUMMY, this.handle("dummy SECOND"));


        lifecycles.registerEventHandler(LifecycleEvents.REGISTER_ANYWHERE_EVENT, event -> {

        });
        lifecycles.registerEventHandler(LifecycleEvents.NON_REGISTRAR_RELATED_EVENT, NonRegistrarEvent::someNonRegistrarRelatedThing);
    }

    private LifecycleEventHandler<RegistrarEvent<DummyResourceRegistrar>> handle(final String out) {
        return event -> {
            System.out.println(out);
        };
    }

}
