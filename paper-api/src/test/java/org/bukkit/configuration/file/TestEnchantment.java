package org.bukkit.configuration.file;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class TestEnchantment extends Enchantment {

    public static void registerEnchantments() {
        Enchantment.registerEnchantment(new TestEnchantment(0, "DUMMY_0"));
        Enchantment.registerEnchantment(new TestEnchantment(1, "DUMMY_1"));
        Enchantment.registerEnchantment(new TestEnchantment(2, "DUMMY_2"));
        Enchantment.registerEnchantment(new TestEnchantment(3, "DUMMY_3"));
        Enchantment.registerEnchantment(new TestEnchantment(4, "DUMMY_4"));
        Enchantment.registerEnchantment(new TestEnchantment(5, "DUMMY_5"));
    }

    private final String name;

    private TestEnchantment(final int id, final String name) {
        super(id);
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return true;
    }

}
