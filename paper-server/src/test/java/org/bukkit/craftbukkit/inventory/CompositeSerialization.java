package org.bukkit.craftbukkit.inventory;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;

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
        stacks.add(new ItemStack(1));
        stacks.add(new ItemStack(2));
        stacks.add(new ItemStack(3));
        stacks.add(new ItemStack(4, 17));
        stacks.add(new ItemStack(5, 63));
        stacks.add(new ItemStack(6, 1, (short) 1));
        stacks.add(new ItemStack(18, 32, (short) 2));

        ItemStack item7 = new ItemStack(256);
        item7.addUnsafeEnchantment(Enchantment.getById(1), 1);
        stacks.add(item7);

        ItemStack item8 = new ItemStack(257);
        item8.addUnsafeEnchantment(Enchantment.getById(2), 2);
        item8.addUnsafeEnchantment(Enchantment.getById(3), 1);
        item8.addUnsafeEnchantment(Enchantment.getById(4), 5);
        item8.addUnsafeEnchantment(Enchantment.getById(5), 4);
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

