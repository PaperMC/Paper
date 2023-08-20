package io.papermc.testplugin;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.dummy.DummyResourceRegistrar;
import io.papermc.paper.plugin.lifecycle.dummy.NonRegistrarEvent;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventHandler;
import io.papermc.paper.plugin.lifecycle.event.registrar.RegistrarEvent;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEvents;
import org.jetbrains.annotations.NotNull;

public class TestPluginBootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {

        context.handleLifecycleEvent(LifecycleEvents.DUMMY, event -> {
            final DummyResourceRegistrar registrar = event.registrar();
            final RegistrarEvent.Reloadable.Cause cause = event.cause();
            System.out.println("dummy hook: " + cause);
        });

        context.handleLifecycleEvent(LifecycleEvents.DUMMY, this.handle("dummy FIRST"));
        context.handleLifecycleEventAsMonitor(LifecycleEvents.DUMMY, this.handle("dummy LAST (monitor)"));
        context.handleLifecycleEvent(LifecycleEvents.DUMMY, this.handle("dummy SECOND"));


        context.handleLifecycleEvent(LifecycleEvents.NON_REGISTRAR_RELATED_EVENT, NonRegistrarEvent::someNonRegistrarRelatedThing);
    }

    private LifecycleEventHandler<RegistrarEvent<DummyResourceRegistrar>> handle(final String out) {
        return event -> {
            System.out.println(out);
        };
    }

}
