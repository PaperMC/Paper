package org.bukkit.enchantments;

import org.jetbrains.annotations.NotNull;

/**
 * A simple wrapper for ease of selecting {@link Enchantment}s
 * @deprecated only for backwards compatibility, EnchantmentWrapper is no longer used.
 */
@Deprecated(since = "1.20.3", forRemoval = true)
public abstract class EnchantmentWrapper extends Enchantment {
    protected EnchantmentWrapper() {
    }

    /**
     * Gets the enchantment bound to this wrapper
     *
     * @return Enchantment
     */
    @NotNull
    public Enchantment getEnchantment() {
        return this;
    }
    // Paper start
    @NotNull
    @Override
    public net.kyori.adventure.text.Component displayName(int level) {
        return getEnchantment().displayName(level);
    }

    @Override
    public @NotNull String translationKey() {
        return getEnchantment().translationKey();
    }

    @Override
    public boolean isTradeable() {
        return getEnchantment().isTradeable();
    }

    @Override
    public boolean isDiscoverable() {
        return getEnchantment().isDiscoverable();
    }

    @Override
    public int getMinModifiedCost(int level) {
        return getEnchantment().getMinModifiedCost(level);
    }

    @Override
    public int getMaxModifiedCost(int level) {
        return getEnchantment().getMaxModifiedCost(level);
    }

    @NotNull
    @Override
    public io.papermc.paper.enchantments.EnchantmentRarity getRarity() {
        return getEnchantment().getRarity();
    }

    @Override
    public float getDamageIncrease(int level, @NotNull org.bukkit.entity.EntityCategory entityCategory) {
        return getEnchantment().getDamageIncrease(level, entityCategory);
    }

    @NotNull
    @Override
    public java.util.Set<org.bukkit.inventory.EquipmentSlot> getActiveSlots() {
        return getEnchantment().getActiveSlots();
    }
    // Paper end
}
