package org.bukkit.craftbukkit.inventory;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemEnchantments;
import io.papermc.paper.datacomponent.item.Tool;
import io.papermc.paper.datacomponent.item.WrittenBookContent;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.keys.BlockTypeKeys;
import io.papermc.paper.registry.set.RegistrySet;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.TriState;
import net.minecraft.world.level.storage.DataVersion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AllFeatures
public class YamlSerializationTest {

    @Test
    void testDataComponents() {
        final ItemStack item = ItemStack.of(Material.WRITTEN_BOOK);
        item.setData(DataComponentTypes.WRITTEN_BOOK_CONTENT, WrittenBookContent.writtenBookContent("Jon", "The Destroyer").addPage(Component.text("hi")).build());
        item.setData(DataComponentTypes.ENCHANTMENTS, ItemEnchantments.itemEnchantments().add(Enchantment.AQUA_AFFINITY, 1).add(Enchantment.DENSITY, 2).build());
        item.setData(DataComponentTypes.GLIDER);
        item.setData(DataComponentTypes.TOOL, Tool.tool().addRule(Tool.rule(RegistrySet.keySet(RegistryKey.BLOCK, BlockTypeKeys.ACACIA_DOOR), 1f, TriState.TRUE)));

        testYamlRoundTrip(item, """
            item:
              ==: org.bukkit.inventory.ItemStack
              DataVersion: %s
              id: minecraft:written_book
              count: 1
              components:
                minecraft:written_book_content: '{author:"The Destroyer",pages:[{raw:"hi"}],title:{raw:"Jon"}}'
                minecraft:glider: '{}'
                minecraft:enchantments: '{"minecraft:aqua_affinity":1,"minecraft:density":2}'
                minecraft:tool: '{rules:[{blocks:"minecraft:acacia_door",correct_for_drops:1b,speed:1.0f}]}'
              schema_version: 1
            """.formatted(Bukkit.getUnsafe().getDataVersion()));
    }

    @Test
    void testItemMeta() {
        final ItemStack item = ItemStack.of(Material.WRITTEN_BOOK);
        item.editMeta(BookMeta.class, (meta) -> {
            meta.setAuthor("The Destroyer");
            meta.setTitle("Jon");
            meta.addPages(Component.text("hi"));

            meta.addEnchant(Enchantment.AQUA_AFFINITY, 1, false);
            meta.addEnchant(Enchantment.DENSITY, 2, false);
        });

        testYamlRoundTrip(item, """
            item:
              ==: org.bukkit.inventory.ItemStack
              DataVersion: %s
              id: minecraft:written_book
              count: 1
              components:
                minecraft:written_book_content: '{author:"The Destroyer",pages:[{raw:"hi"}],title:{raw:"Jon"}}'
                minecraft:enchantments: '{"minecraft:aqua_affinity":1,"minecraft:density":2}'
              schema_version: 1
            """.formatted(Bukkit.getUnsafe().getDataVersion()));
    }

    @Test
    void testUpgrading() {
        // 4189 = 1.21.4
        testYamlUpgrade("""
            item:
              ==: org.bukkit.inventory.ItemStack
              DataVersion: 4189
              id: minecraft:diamond_hoe
              count: 1
              components:
                minecraft:unbreakable: '{show_in_tooltip:false}'
                minecraft:enchantments: '{levels:{"minecraft:sharpness":2},show_in_tooltip:false}'
              schema_version: 1
            """, """
            item:
              ==: org.bukkit.inventory.ItemStack
              DataVersion: %s
              id: minecraft:diamond_hoe
              count: 1
              components:
                minecraft:unbreakable: '{}'
                minecraft:tooltip_display: '{hidden_components:["minecraft:enchantments","minecraft:unbreakable"]}'
                minecraft:enchantments: '{"minecraft:sharpness":2}'
              schema_version: 1
            """.formatted(Bukkit.getUnsafe().getDataVersion()));
    }

    private void testYamlRoundTrip(ItemStack itemStack, String expectedYamlString) {
        final YamlConfiguration out = new YamlConfiguration();
        out.set("item", itemStack);

        final String yamlString = out.saveToString();
        assertEquals(expectedYamlString, yamlString);

        final YamlConfiguration in = new YamlConfiguration();
        try {
            in.loadFromString(yamlString);
        } catch (final InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }

        assertEquals(itemStack, in.getItemStack("item"));
    }

    private void testYamlUpgrade(String oldYamlString, String expectedYamlString) {
        final YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(oldYamlString);
        } catch (final InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }

        try {
            final ConfigurationNode expectedNode = YamlConfigurationLoader.builder().buildAndLoadString(expectedYamlString);
            final ConfigurationNode upgradedNode = YamlConfigurationLoader.builder().buildAndLoadString(config.saveToString());

            Assertions.assertEquals(expectedNode, upgradedNode);
        } catch (ConfigurateException e) {
            Assertions.fail("Failed to read yaml", e);
        }
    }
}
