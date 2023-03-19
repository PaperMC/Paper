package org.bukkit.potion;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.NoSuchElementException;
import org.bukkit.Color;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a potion effect, that can be added to a {@link LivingEntity}. A
 * potion effect has a duration that it will last for, an amplifier that will
 * enhance its effects, and a {@link PotionEffectType}, that represents its
 * effect on an entity.
 */
@SerializableAs("PotionEffect")
public class PotionEffect implements ConfigurationSerializable {
    /**
     * A constant denoting infinite potion duration.
     */
    public static final int INFINITE_DURATION = -1;

    private static final String AMPLIFIER = "amplifier";
    private static final String DURATION = "duration";
    private static final String TYPE = "effect";
    private static final String AMBIENT = "ambient";
    private static final String PARTICLES = "has-particles";
    private static final String ICON = "has-icon";
    private final int amplifier;
    private final int duration;
    private final PotionEffectType type;
    private final boolean ambient;
    private final boolean particles;
    private final boolean icon;

    /**
     * Creates a potion effect.
     * @param type effect type
     * @param duration measured in ticks, see {@link
     *     PotionEffect#getDuration()}
     * @param amplifier the amplifier, see {@link PotionEffect#getAmplifier()}
     * @param ambient the ambient status, see {@link PotionEffect#isAmbient()}
     * @param particles the particle status, see {@link PotionEffect#hasParticles()}
     * @param icon the icon status, see {@link PotionEffect#hasIcon()}
     */
    public PotionEffect(@NotNull PotionEffectType type, int duration, int amplifier, boolean ambient, boolean particles, boolean icon) {
        Preconditions.checkArgument(type != null, "effect type cannot be null");
        this.type = type;
        this.duration = duration;
        this.amplifier = amplifier;
        this.ambient = ambient;
        this.particles = particles;
        this.icon = icon;
    }

    /**
     * Creates a potion effect with no defined color.
     *
     * @param type effect type
     * @param duration measured in ticks, see {@link
     *     PotionEffect#getDuration()}
     * @param amplifier the amplifier, see {@link PotionEffect#getAmplifier()}
     * @param ambient the ambient status, see {@link PotionEffect#isAmbient()}
     * @param particles the particle status, see {@link PotionEffect#hasParticles()}
     */
    public PotionEffect(@NotNull PotionEffectType type, int duration, int amplifier, boolean ambient, boolean particles) {
        this(type, duration, amplifier, ambient, particles, particles);
    }

    /**
     * Creates a potion effect. Assumes that particles are visible
     *
     * @param type effect type
     * @param duration measured in ticks, see {@link
     *     PotionEffect#getDuration()}
     * @param amplifier the amplifier, see {@link PotionEffect#getAmplifier()}
     * @param ambient the ambient status, see {@link PotionEffect#isAmbient()}
     */
    public PotionEffect(@NotNull PotionEffectType type, int duration, int amplifier, boolean ambient) {
        this(type, duration, amplifier, ambient, true);
    }

    /**
     * Creates a potion effect. Assumes ambient is true.
     *
     * @param type Effect type
     * @param duration measured in ticks
     * @param amplifier the amplifier for the effect
     * @see PotionEffect#PotionEffect(PotionEffectType, int, int, boolean)
     */
    public PotionEffect(@NotNull PotionEffectType type, int duration, int amplifier) {
        this(type, duration, amplifier, true);
    }

    /**
     * Constructor for deserialization.
     *
     * @param map the map to deserialize from
     */
    public PotionEffect(@NotNull Map<String, Object> map) {
        this(getEffectType(map), getInt(map, DURATION), getInt(map, AMPLIFIER), getBool(map, AMBIENT, false), getBool(map, PARTICLES, true), getBool(map, ICON, getBool(map, PARTICLES, true)));
    }

    @NotNull
    private static PotionEffectType getEffectType(@NotNull Map<?, ?> map) {
        int type = getInt(map, TYPE);
        PotionEffectType effect = PotionEffectType.getById(type);
        if (effect != null) {
            return effect;
        }
        throw new NoSuchElementException(map + " does not contain " + TYPE);
    }

    private static int getInt(@NotNull Map<?, ?> map, @NotNull Object key) {
        Object num = map.get(key);
        if (num instanceof Integer) {
            return (Integer) num;
        }
        throw new NoSuchElementException(map + " does not contain " + key);
    }

    private static boolean getBool(@NotNull Map<?, ?> map, @NotNull Object key, boolean def) {
        Object bool = map.get(key);
        if (bool instanceof Boolean) {
            return (Boolean) bool;
        }
        return def;
    }

    @Override
    @NotNull
    public Map<String, Object> serialize() {
        return ImmutableMap.<String, Object>builder()
            .put(TYPE, type.getId())
            .put(DURATION, duration)
            .put(AMPLIFIER, amplifier)
            .put(AMBIENT, ambient)
            .put(PARTICLES, particles)
            .put(ICON, icon)
            .build();
    }

    /**
     * Attempts to add the effect represented by this object to the given
     * {@link LivingEntity}.
     *
     * @param entity The entity to add this effect to
     * @return Whether the effect could be added
     * @see LivingEntity#addPotionEffect(PotionEffect)
     */
    public boolean apply(@NotNull LivingEntity entity) {
        return entity.addPotionEffect(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PotionEffect)) {
            return false;
        }
        PotionEffect that = (PotionEffect) obj;
        return this.type.equals(that.type) && this.ambient == that.ambient && this.amplifier == that.amplifier && this.duration == that.duration && this.particles == that.particles && this.icon == that.icon;
    }

    /**
     * Returns the amplifier of this effect. A higher amplifier means the
     * potion effect happens more often over its duration and in some cases
     * has more effect on its target.
     *
     * @return The effect amplifier
     */
    public int getAmplifier() {
        return amplifier;
    }

    /**
     * Returns the duration (in ticks) that this effect will run for when
     * applied to a {@link LivingEntity}.
     *
     * @return The duration of the effect, or {@value #INFINITE_DURATION} if
     * this effect is infinite
     * @see #isInfinite()
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Returns whether or not this potion effect has an infinite duration. Potion
     * effects with infinite durations will display an infinite symbol and never
     * expire unless manually removed.
     *
     * @return whether this duration is infinite or not
     */
    public boolean isInfinite() {
        return duration == INFINITE_DURATION;
    }

    /**
     * Returns whether or not this potion effect has a shorter duration than the
     * provided potion effect.
     * <p>
     * An infinite duration is considered longer than non-infinite durations. If
     * both potion effects have infinite durations, then neither is shorter than
     * the other and this method will return false.
     *
     * @param other the other effect
     * @return true if this effect is shorter than the other, false if longer or equal
     */
    public boolean isShorterThan(@NotNull PotionEffect other) {
        return !isInfinite() && (duration < other.duration || other.isInfinite());
    }

    /**
     * Returns the {@link PotionEffectType} of this effect.
     *
     * @return The potion type of this effect
     */
    @NotNull
    public PotionEffectType getType() {
        return type;
    }

    /**
     * Makes potion effect produce more, translucent, particles.
     *
     * @return if this effect is ambient
     */
    public boolean isAmbient() {
        return ambient;
    }

    /**
     * @return whether this effect has particles or not
     */
    public boolean hasParticles() {
        return particles;
    }

    /**
     * @return color of this potion's particles. May be null if the potion has no particles or defined color.
     * @deprecated color is not part of potion effects
     */
     @Deprecated
     @Nullable
     @Contract("-> null")
    public Color getColor() {
        return null;
    }

    /**
     * @return whether this effect has an icon or not
     */
    public boolean hasIcon() {
        return icon;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + type.hashCode();
        hash = hash * 31 + amplifier;
        hash = hash * 31 + duration;
        hash ^= 0x22222222 >> (ambient ? 1 : -1);
        hash ^= 0x22222222 >> (particles ? 1 : -1);
        hash ^= 0x22222222 >> (icon ? 1 : -1);
        return hash;
    }

    @Override
    public String toString() {
        return type.getName() + (ambient ? ":(" : ":") + duration + "t-x" + amplifier + (ambient ? ")" : "");
    }
}
