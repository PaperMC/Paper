package org.bukkit.potion;

import org.jetbrains.annotations.NotNull;

/**
 * @deprecated only for backwards compatibility, PotionEffectTypeWrapper is no longer used.
 */
@Deprecated(since = "1.20.3", forRemoval = true)
public abstract class PotionEffectTypeWrapper extends PotionEffectType {
    protected PotionEffectTypeWrapper() {
    }

    /**
     * Get the potion type bound to this wrapper.
     *
     * @return The potion effect type
     */
    @NotNull
    public PotionEffectType getType() {
        return this;
    }

    @Override
    public boolean isInstant() {
        return getType().isInstant();
    }

    @NotNull
    @Override
    public org.bukkit.Color getColor() {
        return getType().getColor();
    }
    // Paper start
    @Override
    public @NotNull org.bukkit.NamespacedKey getKey() {
        return this.getType().getKey();
    }

    @Override
    public @NotNull java.util.Map<org.bukkit.attribute.Attribute, org.bukkit.attribute.AttributeModifier> getEffectAttributes() {
        return this.getType().getEffectAttributes();
    }

    @Override
    public double getAttributeModifierAmount(@NotNull org.bukkit.attribute.Attribute attribute, int effectAmplifier) {
        return this.getType().getAttributeModifierAmount(attribute, effectAmplifier);
    }

    @Override
    public @NotNull PotionEffectType.Category getEffectCategory() {
        return this.getType().getEffectCategory();
    }

    @Override
    public @NotNull String translationKey() {
        return this.getType().translationKey();
    }
    // Paper end
}
