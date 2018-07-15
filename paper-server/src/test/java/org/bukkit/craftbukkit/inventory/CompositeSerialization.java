package org.bukkit.craftbukkit.inventory;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Test;


public class CompositeSerialization extends AbstractTestingBase {

    public YamlConfiguration getConfig() {
        return new YamlConfiguration();
    }

    @Test
    public void testSaveRestoreCompositeList() throws InvalidConfigurationException {
        YamlConfiguration out = getConfig();

        List<ItemStack> stacks = new ArrayList<ItemStack>();
        stacks.add(new ItemStack(Material.STONE));
        stacks.add(new ItemStack(Material.GRASS));
        stacks.add(new ItemStack(Material.DIRT));
        stacks.add(new ItemStack(Material.COBBLESTONE, 17));
        stacks.add(new ItemStack(Material.OAK_PLANKS, 63));
        stacks.add(new ItemStack(Material.OAK_SAPLING, 1, (short) 1));
        stacks.add(new ItemStack(Material.OAK_LEAVES, 32, (short) 2));

        ItemStack item7 = new ItemStack(Material.IRON_SHOVEL);
        item7.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 1);
        stacks.add(item7);

        ItemStack item8 = new ItemStack(Material.IRON_PICKAXE);
        item8.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 2);
        item8.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 1);
        item8.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 5);
        item8.addUnsafeEnchantment(Enchantment.OXYGEN, 4);
        stacks.add(item8);

        out.set("composite-list.abc.def", stacks);
        String yaml = out.saveToString();

        YamlConfiguration in = new YamlConfiguration();
        in.loadFromString(yaml);
        List<?> raw = in.getList("composite-list.abc.def");

        assertThat(stacks, hasSize(raw.size()));

        for (int i = 0; i < 9; i++) {
            assertThat(String.valueOf(i), (Object) stacks.get(i), is((Object) raw.get(i)));
        }
    }
}

