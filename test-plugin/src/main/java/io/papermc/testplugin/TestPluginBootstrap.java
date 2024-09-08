package io.papermc.testplugin;

import io.papermc.paper.advancement.CriteriaTrigger;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.CriteriaTriggerRegistryEntry;
import io.papermc.paper.registry.event.RegistryEvents;
import net.kyori.adventure.key.Key;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;

public class TestPluginBootstrap implements PluginBootstrap {

    // this method will be in CriteriaTriggerKeys as a public method
    private static TypedKey<CriteriaTrigger<?>> create(@Subst("key") final String key) {
        return TypedKey.create(RegistryKey.TRIGGER_TYPE, Key.key("test", key));
    }

    static final TypedKey<CriteriaTrigger<?>> CUSTOM_TRIGGER_TYPE = create("trigger");

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        // io.papermc.testplugin.brigtests.Registration.registerViaBootstrap(context);

        context.getLifecycleManager().registerEventHandler(RegistryEvents.TRIGGER_TYPE.freeze(), event -> {
            event.registry().<CriteriaTriggerRegistryEntry.Builder<CustomTriggerInstance>>register(CUSTOM_TRIGGER_TYPE, builder -> {
                builder.deserializer(ignored -> new CustomTriggerInstance(ignored.getAsBoolean()));
            });
        });
    }

}
