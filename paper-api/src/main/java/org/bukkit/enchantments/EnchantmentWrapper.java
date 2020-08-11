package org.bukkit.enchantments;

import org.jetbrains.annotations.NotNull;

/**
 * A simple wrapper for ease of selecting {@link Enchantment}s
 * @deprecated only for backwards compatibility, EnchantmentWrapper is no longer used.
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
    // Paper end
}
