package io.papermc.testplugin;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.DialogKeys;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;

public class TestPluginBootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        // io.papermc.testplugin.brigtests.Registration.registerViaBootstrap(context);

        context.getLifecycleManager().registerEventHandler(RegistryEvents.DIALOG.entryAdd().newHandler(event -> {
            final DialogBase oldBase = event.builder().dialogBase();
            event.builder().dialogBase(DialogBase.create(
                text("New Better Title", NamedTextColor.LIGHT_PURPLE),
                oldBase.externalTitle(),
                oldBase.canCloseWithEscape(),
                oldBase.pause(),
                oldBase.afterAction(),
                oldBase.body(),
                oldBase.inputs()
            ));
        }).filter(DialogKeys.SERVER_LINKS));
    }

}
