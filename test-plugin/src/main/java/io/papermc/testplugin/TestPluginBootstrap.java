package io.papermc.testplugin;

import io.papermc.paper.block.BlockPredicate;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemAdventurePredicate;
import io.papermc.paper.datacomponent.item.ItemEnchantments;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.item.ItemStackTemplate;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.DialogKeys;
import io.papermc.paper.registry.keys.tags.BlockTypeTagKeys;
import io.papermc.paper.registry.set.RegistrySet;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class TestPluginBootstrap implements PluginBootstrap {
    static final Key CUSTOM_DIALOG = Key.key("papermc:custom_dialog");

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        context.getLifecycleManager().registerEventHandler(
            RegistryEvents.DIALOG.compose(),
            event -> event.registry().register(
                DialogKeys.create(CUSTOM_DIALOG),
                builder -> builder
                    .base(DialogBase.builder(Component.text("Title"))
                        .body(List.of(
                            DialogBody.item(ItemStackTemplate.of(ItemType.DIAMOND_PICKAXE, (template) -> {
                                template.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                                    Component.text("Lore!", NamedTextColor.RED)
                                )));
                                template.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
                                template.setData(DataComponentTypes.ENCHANTMENTS, ItemEnchantments.itemEnchantments()
                                    .add(Enchantment.AQUA_AFFINITY, 45)
                                    .build());
                                template.setData(DataComponentTypes.CAN_BREAK, ItemAdventurePredicate.itemAdventurePredicate()
                                    .addPredicate(BlockPredicate.predicate()
                                        .blocks(RegistrySet.keySet(RegistryKey.BLOCK, RegistryKey.BLOCK.typedKey("minecraft:observer")))
                                        .build())
                                    .build());
                            })).build()
                        ))
                        .build())
                    .type(DialogType.notice())
            )
        );
    }

}
