package io.papermc.testplugin;

import io.papermc.paper.advancement.CriteriaTrigger;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.CriteriaTriggerRegistryEntry;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.CriterionTriggerKeys;
import java.io.IOException;
import java.net.URISyntaxException;
import net.kyori.adventure.key.Key;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;

public class TestPluginBootstrap implements PluginBootstrap {

    private static TypedKey<CriteriaTrigger<?>> create(@Subst("key") final String key) {
        return CriterionTriggerKeys.create(Key.key("mm", key));
    }

    static final TypedKey<CriteriaTrigger<?>> CUSTOM_TRIGGER_TYPE = create("trigger");

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        // io.papermc.testplugin.brigtests.Registration.registerViaBootstrap(context);

        context.getLifecycleManager().registerEventHandler(
            RegistryEvents.TRIGGER_TYPE.compose(), event -> {
            event.registry().<CriteriaTriggerRegistryEntry.Builder<CustomTriggerInstance>>register(CUSTOM_TRIGGER_TYPE, builder -> {
                builder.deserializer(obj -> new CustomTriggerInstance(obj.get("is_flying").getAsBoolean()));
            });
        });

        context.getLifecycleManager().registerEventHandler(LifecycleEvents.DATAPACK_DISCOVERY, event -> {
            try {
                event.registrar().discoverPack(TestPluginBootstrap.class.getResource("/pack").toURI(), "test-pack");
            } catch (final URISyntaxException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
