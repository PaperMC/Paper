package io.papermc.testplugin;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.tags.EnchantmentTagKeys;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import io.papermc.paper.tag.PreFlattenTagRegistrar;
import io.papermc.paper.tag.TagEntry;
import java.util.List;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;

public class TestPluginBootstrap implements PluginBootstrap {

    private static final TypedKey<Enchantment> CUSTOM_ENCHANT = TypedKey.create(RegistryKey.ENCHANTMENT, Key.key("test", "enchant"));
    private static final TagKey<ItemType> AXE_PICKAXE = TagKey.create(RegistryKey.ITEM, Key.key("test", "axe_pickaxe"));

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        // io.papermc.testplugin.brigtests.Registration.registerViaBootstrap(context);

        final LifecycleEventManager<BootstrapContext> manager = context.getLifecycleManager();
        manager.registerEventHandler(RegistryEvents.ENCHANTMENT.freeze(), event -> {
            event.registry().register(CUSTOM_ENCHANT, b -> b
                    .description(text("CUSTOM", NamedTextColor.BLUE))
                    .supportedItems(event.getOrCreateTag(AXE_PICKAXE))
                    .weight(100)
                    .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(1, 1))
                    .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(1, 1))
                    .anvilCost(1)
                    .maxLevel(10)
                    .activeSlots(EquipmentSlotGroup.ANY)
            );
        });

        manager.registerEventHandler(LifecycleEvents.TAGS.preFlatten(RegistryKey.ITEM), event -> {
            final PreFlattenTagRegistrar registrar = event.registrar();
            registrar.setTag(AXE_PICKAXE.key(), List.of(
                TagEntry.tagEntry(ItemTypeTagKeys.PICKAXES.key(), true),
                TagEntry.tagEntry(ItemTypeTagKeys.AXES.key(), true)
            ));
        });

        manager.registerEventHandler(LifecycleEvents.TAGS.preFlatten(RegistryKey.ENCHANTMENT), event -> {
            final PreFlattenTagRegistrar registrar = event.registrar();
            registrar.addToTag(
                EnchantmentTagKeys.IN_ENCHANTING_TABLE.key(),
                TagEntry.valueEntry(CUSTOM_ENCHANT.key(), true)
            );
        });
    }

}
