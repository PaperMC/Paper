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

        final LifecycleEventHandler<RegistrarEvent<DummyResourceRegistrar>> handle = this::handle;
        context.handleLifecycleEvent(LifecycleEvents.DUMMY, handle);

        context.handleLifecycleEvent(LifecycleEvents.NON_REGISTRAR_RELATED_EVENT, NonRegistrarEvent::someNonRegistrarRelatedThing);
    }

    private void handle(RegistrarEvent<DummyResourceRegistrar> event) {
    }

}
