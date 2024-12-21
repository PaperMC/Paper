package io.papermc.testplugin;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.PaintingVariantKeys;
import net.kyori.adventure.key.Key;
import org.bukkit.Art;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;

public class TestPluginBootstrap implements PluginBootstrap {

    static final TypedKey<Art> NEW = PaintingVariantKeys.create(Key.key("test:test"));
    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        // io.papermc.testplugin.brigtests.Registration.registerViaBootstrap(context);

        context.getLifecycleManager().registerEventHandler(RegistryEvents.PAINTING_VARIANT.freeze(), event -> {
            event.registry().register(NEW, builder -> {
                builder.assetId(Key.key("wind"))
                    .author(text("ME"))
                    .width(2)
                    .height(2);
            });
        });
    }

}
