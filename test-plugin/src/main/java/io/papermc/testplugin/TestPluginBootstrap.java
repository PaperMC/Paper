package io.papermc.testplugin;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.WolfVariantKeys;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Wolf;
import org.jetbrains.annotations.NotNull;

public class TestPluginBootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        // io.papermc.testplugin.brigtests.Registration.registerViaBootstrap(context);

        context.getLifecycleManager().registerEventHandler(RegistryEvents.ENCHANTMENT.freeze(), event -> {
            final Wolf.Variant v = event.retriever().getOrCreate(WolfVariantKeys.create(Key.key("mm:test")));
            System.out.println("test");
        });
    }

}
