package org.bukkit.potion;

import io.papermc.paper.world.flag.FeatureDependant;
import java.util.List;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.bukkit.Keyed;
import org.bukkit.Registry;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * This list reflects and matches each potion state that can be obtained from
 * the creative mode inventory
 */
@NullMarked
public interface PotionType extends Keyed, FeatureDependant {

    // Start generate - PotionType
    PotionType AWKWARD = getType("awkward");

    PotionType FIRE_RESISTANCE = getType("fire_resistance");

    PotionType HARMING = getType("harming");

    PotionType HEALING = getType("healing");

    PotionType INFESTED = getType("infested");

    PotionType INVISIBILITY = getType("invisibility");

    PotionType LEAPING = getType("leaping");

    PotionType LONG_FIRE_RESISTANCE = getType("long_fire_resistance");

    PotionType LONG_INVISIBILITY = getType("long_invisibility");

    PotionType LONG_LEAPING = getType("long_leaping");

    PotionType LONG_NIGHT_VISION = getType("long_night_vision");

    PotionType LONG_POISON = getType("long_poison");

    PotionType LONG_REGENERATION = getType("long_regeneration");

    PotionType LONG_SLOW_FALLING = getType("long_slow_falling");

    PotionType LONG_SLOWNESS = getType("long_slowness");

    PotionType LONG_STRENGTH = getType("long_strength");

    PotionType LONG_SWIFTNESS = getType("long_swiftness");

    PotionType LONG_TURTLE_MASTER = getType("long_turtle_master");

    PotionType LONG_WATER_BREATHING = getType("long_water_breathing");

    PotionType LONG_WEAKNESS = getType("long_weakness");

    PotionType LUCK = getType("luck");

    PotionType MUNDANE = getType("mundane");

    PotionType NIGHT_VISION = getType("night_vision");

    PotionType OOZING = getType("oozing");

    PotionType POISON = getType("poison");

    PotionType REGENERATION = getType("regeneration");

    PotionType SLOW_FALLING = getType("slow_falling");

    PotionType SLOWNESS = getType("slowness");

    PotionType STRENGTH = getType("strength");

    PotionType STRONG_HARMING = getType("strong_harming");

    PotionType STRONG_HEALING = getType("strong_healing");

    PotionType STRONG_LEAPING = getType("strong_leaping");

    PotionType STRONG_POISON = getType("strong_poison");

    PotionType STRONG_REGENERATION = getType("strong_regeneration");

    PotionType STRONG_SLOWNESS = getType("strong_slowness");

    PotionType STRONG_STRENGTH = getType("strong_strength");

    PotionType STRONG_SWIFTNESS = getType("strong_swiftness");

    PotionType STRONG_TURTLE_MASTER = getType("strong_turtle_master");

    PotionType SWIFTNESS = getType("swiftness");

    PotionType THICK = getType("thick");

    PotionType TURTLE_MASTER = getType("turtle_master");

    PotionType WATER = getType("water");

    PotionType WATER_BREATHING = getType("water_breathing");

    PotionType WEAKNESS = getType("weakness");

    PotionType WEAVING = getType("weaving");

    PotionType WIND_CHARGED = getType("wind_charged");
    // End generate - PotionType

    private static PotionType getType(final @KeyPattern.Value String key) {
        return Registry.POTION.getOrThrow(Key.key(Key.MINECRAFT_NAMESPACE, key));
    }

    /**
     * @return the potion effect type of this potion type
     * @deprecated Potions can have multiple effects use {@link #getPotionEffects()}
     */
    @Deprecated(since = "1.20.2")
    default @Nullable PotionEffectType getEffectType() {
        final List<PotionEffect> effects = this.getPotionEffects();
        return effects.isEmpty() ? null : effects.getFirst().getType();
    }

    /**
     * @return a list of all effects this potion type has
     */
    List<PotionEffect> getPotionEffects();

    /**
     * @return if this potion type is instant
     * @deprecated PotionType can have multiple effects, some of which can be instant and others not.
     * Use {@link PotionEffectType#isInstant()} in combination with {@link #getPotionEffects()} and {@link PotionEffect#getType()}
     */
    @Deprecated(since = "1.20.2")
    boolean isInstant();

    /**
     * Checks if the potion type has an upgraded state.
     * This refers to whether or not the potion type can be Tier 2,
     * such as Potion of Fire Resistance II.
     *
     * @return true if the potion type can be upgraded;
     */
    boolean isUpgradeable();

    /**
     * Checks if the potion type has an extended state.
     * This refers to the extended duration potions
     *
     * @return true if the potion type can be extended
     */
    boolean isExtendable();

    int getMaxLevel();

    /**
     * @param effectType the effect to get by
     * @return the matching potion type
     * @deprecated Misleading
     */
    @Deprecated(since = "1.9")
    static @Nullable PotionType getByEffect(@Nullable PotionEffectType effectType) {
        if (effectType == null) return WATER;
        for (PotionType type : Registry.POTION) {
            if (effectType.equals(type.getEffectType())) {
                return type;
            }
        }
        return null;
    }

}
