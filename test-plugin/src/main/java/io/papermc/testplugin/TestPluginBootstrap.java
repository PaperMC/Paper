package io.papermc.testplugin;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.EnchantmentKeys;
import io.papermc.paper.registry.keys.GameEventKeys;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.set.RegistrySet;
import io.papermc.paper.registry.tag.TagKey;
import java.util.Collections;
import java.util.List;
import net.kyori.adventure.key.Key;
import org.bukkit.GameEvent;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;

public class TestPluginBootstrap implements PluginBootstrap {

    static final TypedKey<GameEvent> NEW_GAME_EVENT = TypedKey.create(RegistryKey.GAME_EVENT, Key.key("mm:new_game_event"));
    static final TypedKey<Enchantment> NEW_ENCHANT = TypedKey.create(RegistryKey.ENCHANTMENT, Key.key("machine:test"));

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        // io.papermc.testplugin.brigtests.Registration.registerViaBootstrap(context);

        context.getLifecycleManager().registerEventHandler(RegistryEvents.ENCHANTMENT.freeze().newHandler(event -> {
            event.registry().register(NEW_ENCHANT, builder -> {
                builder.description(text("Epic Enchant!"))
                    .supportedItems(event.getOrCreateTag(ItemTypeTagKeys.SWORDS))
                    .maxLevel(10)
                    .weight(1024)
                    .activeSlots(List.of(EquipmentSlotGroup.ANY))
                    .anvilCost(1)
                    .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(1, 1))
                    .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(2, 2));
            });
        }));

        context.getLifecycleManager().registerEventHandler(RegistryEvents.ENCHANTMENT.entryAdd().newHandler(event -> {
            event.builder()
                .description(text("Custom efficiency!"));
        }).filter(EnchantmentKeys.EFFICIENCY));

        context.getLifecycleManager().registerEventHandler(RegistryEvents.GAME_EVENT.freeze(), event -> {
            event.registry().register(NEW_GAME_EVENT, builder -> {
                builder.range(3);
            });
        });
        context.getLifecycleManager().registerEventHandler(RegistryEvents.GAME_EVENT.entryAdd().newHandler(event -> {
            event.builder().range(3);
            event.getOrCreateTag(TagKey.create(RegistryKey.ITEM, Key.key("mm:tag")));
        }).filter(GameEventKeys.CONTAINER_OPEN));
    }

}
