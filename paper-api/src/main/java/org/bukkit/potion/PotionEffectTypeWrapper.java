package org.bukkit.potion;

import org.jetbrains.annotations.NotNull;

/**
 * @deprecated only for backwards compatibility, PotionEffectTypeWrapper is no longer used.
 */
@Deprecated
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
}
