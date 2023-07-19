package io.papermc.testplugin;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.register.dummy.DummyResourceRegistrar;
import io.papermc.paper.plugin.register.dummy.NonRegistrarEvent;
import io.papermc.paper.plugin.register.RegistrarEvent;
import io.papermc.paper.plugin.register.event.RegisterEvents;
import org.jetbrains.annotations.NotNull;

public class TestPluginBootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {

        context.registerHook(RegisterEvents.DUMMY, event -> {
            final DummyResourceRegistrar registrar = event.registrar();
            final RegistrarEvent.Reloadable.Cause cause = event.cause();
            System.out.println("dummy hook: " + cause);
        });

        final io.papermc.paper.plugin.register.event.RegisterEventHandler<io.papermc.paper.plugin.register.RegistrarEvent<io.papermc.paper.plugin.register.dummy.DummyResourceRegistrar>> handle = this::handle;
        context.registerHook(RegisterEvents.DUMMY, handle);

        context.registerHook(RegisterEvents.NON_REGISTRAR_RELATED_EVENT, NonRegistrarEvent::someNonRegistrarRelatedThing);
    }

    private void handle(RegistrarEvent<DummyResourceRegistrar> event) {
    }

}
