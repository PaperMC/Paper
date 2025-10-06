package org.bukkit.potion;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import java.util.Locale;
import org.bukkit.Color;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Translatable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a type of potion and its effect on an entity.
 */
public abstract class PotionEffectType implements Keyed, Translatable, net.kyori.adventure.translation.Translatable, io.papermc.paper.world.flag.FeatureDependant { // Paper - implement Translatable & feature flag API
    private static final BiMap<Integer, PotionEffectType> ID_MAP = HashBiMap.create();

    /**
     * Increases movement speed.
     */
    public static final PotionEffectType SPEED = getPotionEffectType(1, "speed");

    /**
     * Decreases movement speed.
     */
    public static final PotionEffectType SLOWNESS = getPotionEffectType(2, "slowness");

    /**
     * Increases dig speed.
     */
    public static final PotionEffectType HASTE = getPotionEffectType(3, "haste");

    /**
     * Decreases dig speed.
     */
    public static final PotionEffectType MINING_FATIGUE = getPotionEffectType(4, "mining_fatigue");

    /**
     * Increases damage dealt.
     */
    public static final PotionEffectType STRENGTH = getPotionEffectType(5, "strength");

    /**
     * Heals an entity.
     */
    public static final PotionEffectType INSTANT_HEALTH = getPotionEffectType(6, "instant_health");

    /**
     * Hurts an entity.
     */
    public static final PotionEffectType INSTANT_DAMAGE = getPotionEffectType(7, "instant_damage");

    /**
     * Increases jump height.
     */
    public static final PotionEffectType JUMP_BOOST = getPotionEffectType(8, "jump_boost");

    /**
     * Warps vision on the client.
     */
    public static final PotionEffectType NAUSEA = getPotionEffectType(9, "nausea");

    /**
     * Regenerates health.
     */
    public static final PotionEffectType REGENERATION = getPotionEffectType(10, "regeneration");

    /**
     * Decreases damage dealt to an entity.
     */
    public static final PotionEffectType RESISTANCE = getPotionEffectType(11, "resistance");

    /**
     * Stops fire damage.
     */
    public static final PotionEffectType FIRE_RESISTANCE = getPotionEffectType(12, "fire_resistance");

    /**
     * Allows breathing underwater.
     */
    public static final PotionEffectType WATER_BREATHING = getPotionEffectType(13, "water_breathing");

    /**
     * Grants invisibility.
     */
    public static final PotionEffectType INVISIBILITY = getPotionEffectType(14, "invisibility");

    /**
     * Blinds an entity.
     */
    public static final PotionEffectType BLINDNESS = getPotionEffectType(15, "blindness");

    /**
     * Allows an entity to see in the dark.
     */
    public static final PotionEffectType NIGHT_VISION = getPotionEffectType(16, "night_vision");

    /**
     * Increases hunger.
     */
    public static final PotionEffectType HUNGER = getPotionEffectType(17, "hunger");

    /**
     * Decreases damage dealt by an entity.
     */
    public static final PotionEffectType WEAKNESS = getPotionEffectType(18, "weakness");

    /**
     * Deals damage to an entity over time.
     */
    public static final PotionEffectType POISON = getPotionEffectType(19, "poison");

    /**
     * Deals damage to an entity over time and gives the health to the
     * shooter.
     */
    public static final PotionEffectType WITHER = getPotionEffectType(20, "wither");

    /**
     * Increases the maximum health of an entity.
     */
    public static final PotionEffectType HEALTH_BOOST = getPotionEffectType(21, "health_boost");

    /**
     * Increases the maximum health of an entity with health that cannot be
     * regenerated, but is refilled every 30 seconds.
     */
    public static final PotionEffectType ABSORPTION = getPotionEffectType(22, "absorption");

    /**
     * Increases the food level of an entity each tick.
     */
    public static final PotionEffectType SATURATION = getPotionEffectType(23, "saturation");

    /**
     * Outlines the entity so that it can be seen from afar.
     */
    public static final PotionEffectType GLOWING = getPotionEffectType(24, "glowing");

    /**
     * Causes the entity to float into the air.
     */
    public static final PotionEffectType LEVITATION = getPotionEffectType(25, "levitation");

    /**
     * Loot table luck.
     */
    public static final PotionEffectType LUCK = getPotionEffectType(26, "luck");

    /**
     * Loot table unluck.
     */
    public static final PotionEffectType UNLUCK = getPotionEffectType(27, "unluck");

    /**
     * Slows entity fall rate.
     */
    public static final PotionEffectType SLOW_FALLING = getPotionEffectType(28, "slow_falling");

    /**
     * Effects granted by a nearby conduit. Includes enhanced underwater abilities.
     */
    public static final PotionEffectType CONDUIT_POWER = getPotionEffectType(29, "conduit_power");

    /**
     * Increases underwater movement speed.<br>
     * Squee'ek uh'k kk'kkkk squeek eee'eek.
     */
    public static final PotionEffectType DOLPHINS_GRACE = getPotionEffectType(30, "dolphins_grace");

    /**
     * Triggers an ominous event when the player enters a village or trial chambers.<br>
     * oof.
     */
    public static final PotionEffectType BAD_OMEN = getPotionEffectType(31, "bad_omen");

    /**
     * Reduces the cost of villager trades.<br>
     * \o/.
     */
    public static final PotionEffectType HERO_OF_THE_VILLAGE = getPotionEffectType(32, "hero_of_the_village");

    /**
     * Causes the player's vision to dim occasionally.
     */
    public static final PotionEffectType DARKNESS = getPotionEffectType(33, "darkness");

    /**
     * Causes trial spawners to become ominous.
     */
    public static final PotionEffectType TRIAL_OMEN = getPotionEffectType(34, "trial_omen");

    /**
     * Triggers a raid when a player enters a village.
     */
    public static final PotionEffectType RAID_OMEN = getPotionEffectType(35, "raid_omen");

    /**
     * Emits a wind burst upon death.
     */
    public static final PotionEffectType WIND_CHARGED = getPotionEffectType(36, "wind_charged");

    /**
     * Creates cobwebs upon death.
     */
    public static final PotionEffectType WEAVING = getPotionEffectType(37, "weaving");

    /**
     * Causes slimes to spawn upon death.
     */
    public static final PotionEffectType OOZING = getPotionEffectType(38, "oozing");

    /**
     * Chance of spawning silverfish when hurt.
     */
    public static final PotionEffectType INFESTED = getPotionEffectType(39, "infested");

    @NotNull
    private static PotionEffectType getPotionEffectType(int typeId, @NotNull String key) {
        PotionEffectType potionEffectType = Registry.EFFECT.getOrThrow(NamespacedKey.minecraft(key));

        if (typeId > 0) {
            ID_MAP.put(typeId, potionEffectType);
        }
        return potionEffectType;
    }

    /**
     * Creates a PotionEffect from this PotionEffectType, applying duration
     * modifiers and checks.
     *
     * @param duration time in ticks
     * @param amplifier the effect's amplifier
     * @return a resulting potion effect
     * @see PotionBrewer#createEffect(PotionEffectType, int, int)
     */
    @NotNull
    public abstract PotionEffect createEffect(int duration, int amplifier);

    /**
     * Returns whether the effect of this type happens once, immediately.
     *
     * @return whether this type is normally instant
     */
    public abstract boolean isInstant();

    /**
     * Returns the {@link PotionEffectTypeCategory category} of this effect type.
     *
     * @return the category
     */
    @NotNull
    public abstract PotionEffectTypeCategory getCategory();

    /**
     * Returns the color of this effect type.
     *
     * @return the color
     */
    @NotNull
    public abstract Color getColor();

    /**
     * Returns the duration modifier applied to effects of this type.
     *
     * @return duration modifier
     * @deprecated unused, always 1.0
     */
    @Deprecated(since = "1.14", forRemoval = true)
    public abstract double getDurationModifier();

    /**
     * Returns the unique ID of this type.
     *
     * @return Unique ID
     * @deprecated use {@link #key()}
     */
    @Deprecated(since = "1.6.2", forRemoval = true) // Paper
    public abstract int getId();

    /**
     * Returns the name of this effect type.
     *
     * @return The name of this effect type
     * @deprecated only for backwards compatibility, use {@link #getKey()} instead.
     */
    @NotNull
    @Deprecated(since = "1.20.3")
    public abstract String getName();

    /**
     * Gets the PotionEffectType at the specified key
     *
     * @param key key to fetch
     * @return Resulting PotionEffectType, or null if not found
     * @deprecated only for backwards compatibility, use {@link Registry#get(NamespacedKey)} instead.
     */
    @Contract("null -> null")
    @Nullable
    @Deprecated(since = "1.20.3")
    public static PotionEffectType getByKey(@Nullable NamespacedKey key) {
        if (key == null) {
            return null;
        }

        return Registry.EFFECT.get(key);
    }

    /**
     * Gets the effect type specified by the unique id.
     *
     * @param id Unique ID to fetch
     * @return Resulting type, or null if not found.
     * @apiNote Internal Use Only
     */
    @org.jetbrains.annotations.ApiStatus.Internal // Paper
    @Nullable
    public static PotionEffectType getById(int id) {
        PotionEffectType type = ID_MAP.get(id);

        if (type != null) {
            return type;
        }

        for (PotionEffectType other : Registry.EFFECT) {
            if (other.getId() == id) {
                ID_MAP.put(id, other);
                return other;
            }
        }

        return null;
    }

    /**
     * Gets the effect type specified by the given name.
     *
     * @param name Name of PotionEffectType to fetch
     * @return Resulting PotionEffectType, or null if not found.
     * @deprecated only for backwards compatibility, use {@link Registry#get(NamespacedKey)} instead.
     */
    @Nullable
    @Deprecated(since = "1.20.3")
    public static PotionEffectType getByName(@NotNull String name) {
        Preconditions.checkArgument(name != null, "name cannot be null");
        return Registry.EFFECT.get(NamespacedKey.fromString(name.toLowerCase(Locale.ROOT)));
    }

    /**
     * @return an array of all known PotionEffectTypes.
     * @deprecated use {@link Registry#iterator()}.
     */
    @NotNull
    @Deprecated(since = "1.20.3")
    public static PotionEffectType[] values() {
        return Lists.newArrayList(Registry.EFFECT).toArray(new PotionEffectType[0]);
    }

    // Paper start
    /**
     * Gets the effect attributes in an immutable map.
     *
     * @return the attribute map
     */
    public abstract @NotNull java.util.Map<org.bukkit.attribute.Attribute, org.bukkit.attribute.AttributeModifier> getEffectAttributes();

    /**
     * Gets the true modifier amount based on the effect amplifier.
     *
     * @param attribute the attribute
     * @param effectAmplifier the effect amplifier (0 indexed)
     * @return the modifier amount
     * @throws IllegalArgumentException if the supplied attribute is not present in the map from {@link #getEffectAttributes()}
     */
    public abstract double getAttributeModifierAmount(@NotNull org.bukkit.attribute.Attribute attribute, int effectAmplifier);

    /**
     * Gets the category of this effect
     *
     * @return the category
     */
    public abstract @NotNull PotionEffectType.Category getEffectCategory();

    /**
     * Category of {@link PotionEffectType}s
     */
    public enum Category {

        BENEFICIAL(net.kyori.adventure.text.format.NamedTextColor.BLUE),
        HARMFUL(net.kyori.adventure.text.format.NamedTextColor.RED),
        NEUTRAL(net.kyori.adventure.text.format.NamedTextColor.BLUE);

        private final net.kyori.adventure.text.format.TextColor color;

        Category(net.kyori.adventure.text.format.TextColor color) {
            this.color = color;
        }

        /**
         * Gets the text color used when displaying potions
         * of this category.
         *
         * @return the text color
         */
        @NotNull
        public net.kyori.adventure.text.format.TextColor getColor() {
            return color;
        }
    }
    // Paper end
}
