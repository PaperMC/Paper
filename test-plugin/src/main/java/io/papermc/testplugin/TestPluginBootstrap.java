package io.papermc.testplugin;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.GameEventKeys;
import net.kyori.adventure.key.Key;
import org.bukkit.GameEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.jetbrains.annotations.NotNull;

@DefaultQualifier(NonNull.class)
public class TestPluginBootstrap implements PluginBootstrap {

    static final TypedKey<GameEvent> NEW_EVENT = TypedKey.create(RegistryKey.GAME_EVENT, Key.key("machine_maker", "best_event"));

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        // io.papermc.testplugin.brigtests.Registration.registerViaBootstrap(context);

        final LifecycleEventManager<BootstrapContext> lifecycleManager = context.getLifecycleManager();
        lifecycleManager.registerEventHandler(RegistryEvents.GAME_EVENT.entryAdd().newHandler(event -> {
            event.builder().range(event.builder().range() * 40);
        }).priority(10).onlyFor(GameEventKeys.BLOCK_OPEN));
        lifecycleManager.registerEventHandler(RegistryEvents.GAME_EVENT.freeze(), event -> {
            event.registry().register(NEW_EVENT, builder -> {
                builder.range(2);
            });
        });
    }
}
