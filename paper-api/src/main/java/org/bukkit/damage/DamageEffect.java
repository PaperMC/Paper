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
public interface DamageEffect {

    /**
     * The default damage effect.
     */
    public static final DamageEffect HURT = getDamageEffect("hurt");
    /**
     * Thorns.
     */
    public static final DamageEffect THORNS = getDamageEffect("thorns");
    /**
     * Drowning.
     */
    public static final DamageEffect DROWNING = getDamageEffect("drowning");
    /**
     * A single burn tick (fire, lava, etc.).
     */
    public static final DamageEffect BURNING = getDamageEffect("burning");
    /**
     * Poked by a berry bush.
     */
    public static final DamageEffect POKING = getDamageEffect("poking");
    /**
     * Freeze tick (powder snow).
     */
    public static final DamageEffect FREEZING = getDamageEffect("freezing");

    @NotNull
    private static DamageEffect getDamageEffect(@NotNull String key) {
        return Preconditions.checkNotNull(Bukkit.getUnsafe().getDamageEffect(key), "No DamageEffect found for %s. This is a bug.", key);
    }

    /**
     * Get the {@link Sound} played for this {@link DamageEffect}.
     *
     * @return the sound
     */
    @NotNull
    public Sound getSound();
}
