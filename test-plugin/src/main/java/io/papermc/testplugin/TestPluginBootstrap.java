package io.papermc.testplugin;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.resource.RegisterEvent;
import io.papermc.paper.plugin.resource.dummy.DummyReloadableResourceRegistrar;
import io.papermc.paper.plugin.resource.hook.RegisterHooks;
import org.jetbrains.annotations.NotNull;

public class TestPluginBootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {

        RegisterHooks.DUMMY.add(context, event -> {
            final DummyReloadableResourceRegistrar registrar = event.registrar();
            final RegisterEvent.Reloadable.Cause cause = event.cause();
        });
    }

}
