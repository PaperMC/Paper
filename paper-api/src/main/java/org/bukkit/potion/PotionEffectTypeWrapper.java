package org.bukkit.potion;

import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public class PotionEffectTypeWrapper extends PotionEffectType {
    protected PotionEffectTypeWrapper(int id, @NotNull String name) {
        super(id, NamespacedKey.minecraft(name));
    }

    @Override
    public double getDurationModifier() {
        return getType().getDurationModifier();
    }

    @NotNull
    @Override
    public String getName() {
        return getType().getName();
    }

    /**
     * Get the potion type bound to this wrapper.
     *
     * @return The potion effect type
     */
    @NotNull
    public PotionEffectType getType() {
        return PotionEffectType.getById(getId());
    }

    @Override
    public boolean isInstant() {
        return getType().isInstant();
    }

    @NotNull
    @Override
    public Color getColor() {
        return getType().getColor();
    }
}
