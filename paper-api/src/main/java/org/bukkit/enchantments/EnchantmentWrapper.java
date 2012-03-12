package org.bukkit.enchantments;

import org.bukkit.inventory.ItemStack;

/**
 * A simple wrapper for ease of selecting {@link Enchantment}s
 */
public class EnchantmentWrapper extends Enchantment {
    public EnchantmentWrapper(int id) {
        super(id);
    }

    /**
     * Gets the enchantment bound to this wrapper
     *
     * @return Enchantment
     */
    public Enchantment getEnchantment() {
        return Enchantment.getById(getId());
    }

    @Override
    public int getMaxLevel() {
        return getEnchantment().getMaxLevel();
    }

    @Override
    public int getStartLevel() {
        return getEnchantment().getStartLevel();
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return getEnchantment().getItemTarget();
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return getEnchantment().canEnchantItem(item);
    }

    @Override
    public String getName() {
        return getEnchantment().getName();
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return getEnchantment().conflictsWith(other);
    }
}
