package io.papermc.testplugin;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.event.dummy.DummyResourceRegistrar;
import io.papermc.paper.plugin.event.dummy.NonRegistrarEvent;
import io.papermc.paper.plugin.event.RegistrarEvent;
import io.papermc.paper.plugin.event.hook.Hook;
import io.papermc.paper.plugin.event.hook.RegisterHooks;
import org.bukkit.util.Consumer;
import org.jetbrains.annotations.NotNull;

public class TestPluginBootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {

        RegisterHooks.DUMMY.add(context, event -> {
            final DummyResourceRegistrar registrar = event.registrar();
            final RegistrarEvent.Reloadable.Cause cause = event.cause();
            System.out.println("dummy hook: " + cause);
        });

        final Hook<RegistrarEvent<DummyResourceRegistrar>> handle = this::handle;
        RegisterHooks.DUMMY.add(context, handle);

        RegisterHooks.NON_REGISTRAR_RELATED_EVENT.add(context, NonRegistrarEvent::someNonRegistrarRelatedThing);
    }

    private void handle(RegistrarEvent<DummyResourceRegistrar> event) {
    }

}
