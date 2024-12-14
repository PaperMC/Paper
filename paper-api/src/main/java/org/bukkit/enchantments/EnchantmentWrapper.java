package org.bukkit.enchantments;

import org.jetbrains.annotations.NotNull;

/**
 * A simple wrapper for ease of selecting {@link Enchantment}s
 * @deprecated only for backwards compatibility, EnchantmentWrapper is no longer used.
 * @since 1.1.0 R1
 */
@Deprecated(since = "1.20.3")
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
    /**
     * @since 1.16.5
     */
    // Paper start
    @NotNull
    @Override
    public net.kyori.adventure.text.Component displayName(int level) {
        return getEnchantment().displayName(level);
    }

    /**
     * @since 1.17.1
     */
    @Override
    public @NotNull String translationKey() {
        return getEnchantment().translationKey();
    }

    /**
     * @since 1.16.5
     */
    @Override
    public boolean isTradeable() {
        return getEnchantment().isTradeable();
    }

    /**
     * @since 1.16.5
     */
    @Override
    public boolean isDiscoverable() {
        return getEnchantment().isDiscoverable();
    }

    /**
     * @since 1.20.2
     */
    @Override
    public int getMinModifiedCost(int level) {
        return getEnchantment().getMinModifiedCost(level);
    }

    /**
     * @since 1.20.2
     */
    @Override
    public int getMaxModifiedCost(int level) {
        return getEnchantment().getMaxModifiedCost(level);
    }

    /**
     * @since 1.16.5
     */
    @NotNull
    @Override
    public io.papermc.paper.enchantments.EnchantmentRarity getRarity() {
        return getEnchantment().getRarity();
    }

    /**
     * @since 1.16.5
     */
    @Override
    public float getDamageIncrease(int level, @NotNull org.bukkit.entity.EntityCategory entityCategory) {
        return getEnchantment().getDamageIncrease(level, entityCategory);
    }

    /**
     * @since 1.16.5
     */
    @NotNull
    @Override
    public java.util.Set<org.bukkit.inventory.EquipmentSlot> getActiveSlots() {
        return getEnchantment().getActiveSlots();
    }
    // Paper end
}
