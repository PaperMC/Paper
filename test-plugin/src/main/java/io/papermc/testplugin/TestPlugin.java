package io.papermc.testplugin;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemEnchantments;
import io.papermc.paper.datacomponent.item.Tool;
import io.papermc.paper.datacomponent.item.WrittenBookContent;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.keys.BlockTypeKeys;
import io.papermc.paper.registry.set.RegistrySet;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.TriState;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        // Todo: Obtain config files for RTT testing
        final YamlConfiguration yamlConfiguration = new YamlConfiguration();

        final ItemStack itemStack = ItemStack.of(Material.WRITTEN_BOOK);
        itemStack.setData(DataComponentTypes.WRITTEN_BOOK_CONTENT, WrittenBookContent.writtenBookContent("Jon", "The Destroyer").addPage(Component.text("hi")).build());
        itemStack.setData(DataComponentTypes.ENCHANTMENTS, ItemEnchantments.itemEnchantments().add(Enchantment.AQUA_AFFINITY, 1).add(Enchantment.DENSITY, 2).build());
        itemStack.setData(DataComponentTypes.GLIDER);
        itemStack.setData(DataComponentTypes.TOOL, Tool.tool().addRule(Tool.rule(RegistrySet.keySet(RegistryKey.BLOCK, BlockTypeKeys.ACACIA_DOOR), 1f, TriState.TRUE)));
        yamlConfiguration.set("item", itemStack);
        String yamlString = yamlConfiguration.saveToString();
        System.out.println(yamlString);

        final YamlConfiguration yaml2 = new YamlConfiguration();
        try {
            yaml2.loadFromString(yamlString);
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }

        System.out.println(yaml2.get("item"));

        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);
    }
}
