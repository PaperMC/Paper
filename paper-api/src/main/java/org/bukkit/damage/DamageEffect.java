package org.bukkit.damage;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a type of effect that occurs when damage is inflicted. Currently,
 * effects only determine the sound that plays.
 */
@ApiStatus.Experimental
public enum DamageEffect {

    /**
     * The default damage effect.
     */
    HURT,
    /**
     * Thorns.
     */
    THORNS,
    /**
     * Drowning.
     */
    DROWNING,
    /**
     * A single burn tick (fire, lava, etc.).
     */
    BURNING,
    /**
     * Poked by a berry bush.
     */
    POKING,
    /**
     * Freeze tick (powder snow).
     */
    FREEZING;

    @NotNull
    @Deprecated
    private static DamageEffect getDamageEffect(@NotNull String key) {
        return Preconditions.checkNotNull(Bukkit.getUnsafe().getDamageEffect(key), "No DamageEffect found for %s. This is a bug.", key);
    }

    /**
     * Get the {@link Sound} played for this {@link DamageEffect}.
     *
     * @return the sound
     */
    @NotNull
    public Sound getSound() {
        return Bukkit.getUnsafe().getSoundForDamageEffect(this);
    }
}
