package io.papermc.testplugin;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.DimensionTypeKeys;
import io.papermc.paper.registry.keys.WorldPresetKeys;
import io.papermc.paper.world.worldgen.LevelStem;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class TestPluginBootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        // io.papermc.testplugin.brigtests.Registration.registerViaBootstrap(context);

        final TypedKey<LevelStem> NEW = TypedKey.create(RegistryKey.LEVEL_STEM, Key.key("mm:test"));
        context.getLifecycleManager().registerEventHandler(RegistryEvents.LEVEL_STEM.compose(), event -> {
            event.registry().register(NEW, b -> {
                b.type(DimensionTypeKeys.OVERWORLD)
                    .copyGeneratorFrom(WorldPresetKeys.AMPLIFIED, TypedKey.create(RegistryKey.LEVEL_STEM, Key.key("minecraft:the_nether")));
            });
        });
    }

}
