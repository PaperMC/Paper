package org.bukkit.potion;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.LivingEntity;

/**
 * Represents a potion effect, that can be added to a {@link LivingEntity}. A
 * potion effect has a duration that it will last for, an amplifier that will
 * enhance its effects, and a {@link PotionEffectType}, that represents its
 * effect on an entity.
 */
public class PotionEffect {
    private final int amplifier;
    private final int duration;
    private final PotionEffectType type;

    public PotionEffect(PotionEffectType type, int duration, int amplifier) {
        Validate.notNull(type, "effect type cannot be null");
        this.type = type;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    /**
     * Attempts to add the effect represented by this object to the given
     * {@link LivingEntity}.
     *
     * @see LivingEntity#addPotionEffect(PotionEffect)
     * @param entity The entity to add this effect to
     * @return Whether the effect could be added
     */
    public boolean apply(LivingEntity entity) {
        return entity.addPotionEffect(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PotionEffect other = (PotionEffect) obj;
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        return true;
    }

    /**
     * Returns the amplifier of this effect. A higher amplifier means the potion
     * effect happens more often over its duration and in some cases has more
     * effect on its target.
     * @return The effect amplifier
     */
    public int getAmplifier() {
        return amplifier;
    }

    /**
     * Returns the duration (in ticks) that this effect will run for when
     * applied to a {@link LivingEntity}.
     * @return The duration of the effect
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Returns the {@link PotionEffectType} of this effect.
     *
     * @return The potion type of this effect
     */
    public PotionEffectType getType() {
        return type;
    }

    @Override
    public int hashCode() {
        return 31 + ((type == null) ? 0 : type.hashCode());
    };
}
