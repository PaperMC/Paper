package io.papermc.paper.registry.keys;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Vanilla keys for {@link RegistryKey#MOB_EFFECT}.
 *
 * @apiNote The fields provided here are a direct representation of
 * what is available from the vanilla game source. They may be
 * changed (including removals) on any Minecraft version
 * bump, so cross-version compatibility is not provided on the
 * same level as it is on most of the other API.
 */
@SuppressWarnings({
        "unused",
        "SpellCheckingInspection"
})
@GeneratedFrom("1.20.4")
@ApiStatus.Experimental
public final class MobEffectKeys {
    /**
     * {@code minecraft:speed}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotionEffectType> SPEED = create(key("speed"));

    /**
     * {@code minecraft:slowness}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotionEffectType> SLOWNESS = create(key("slowness"));

    /**
     * {@code minecraft:haste}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotionEffectType> HASTE = create(key("haste"));

    /**
     * {@code minecraft:mining_fatigue}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotionEffectType> MINING_FATIGUE = create(key("mining_fatigue"));

    /**
     * {@code minecraft:strength}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotionEffectType> STRENGTH = create(key("strength"));

    /**
     * {@code minecraft:instant_health}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotionEffectType> INSTANT_HEALTH = create(key("instant_health"));

    /**
     * {@code minecraft:instant_damage}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotionEffectType> INSTANT_DAMAGE = create(key("instant_damage"));

    /**
     * {@code minecraft:jump_boost}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotionEffectType> JUMP_BOOST = create(key("jump_boost"));

    /**
     * {@code minecraft:nausea}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotionEffectType> NAUSEA = create(key("nausea"));

    /**
     * {@code minecraft:regeneration}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotionEffectType> REGENERATION = create(key("regeneration"));

    /**
     * {@code minecraft:resistance}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotionEffectType> RESISTANCE = create(key("resistance"));

    /**
     * {@code minecraft:fire_resistance}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotionEffectType> FIRE_RESISTANCE = create(key("fire_resistance"));

    /**
     * {@code minecraft:water_breathing}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotionEffectType> WATER_BREATHING = create(key("water_breathing"));

    /**
     * {@code minecraft:invisibility}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotionEffectType> INVISIBILITY = create(key("invisibility"));

    /**
     * {@code minecraft:blindness}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotionEffectType> BLINDNESS = create(key("blindness"));

    /**
     * {@code minecraft:night_vision}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotionEffectType> NIGHT_VISION = create(key("night_vision"));

    /**
     * {@code minecraft:hunger}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotionEffectType> HUNGER = create(key("hunger"));

    /**
     * {@code minecraft:weakness}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotionEffectType> WEAKNESS = create(key("weakness"));

    /**
     * {@code minecraft:poison}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotionEffectType> POISON = create(key("poison"));

    /**
     * {@code minecraft:wither}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotionEffectType> WITHER = create(key("wither"));

    /**
     * {@code minecraft:health_boost}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotionEffectType> HEALTH_BOOST = create(key("health_boost"));

    /**
     * {@code minecraft:absorption}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotionEffectType> ABSORPTION = create(key("absorption"));

    /**
     * {@code minecraft:saturation}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotionEffectType> SATURATION = create(key("saturation"));

    /**
     * {@code minecraft:glowing}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotionEffectType> GLOWING = create(key("glowing"));

    /**
     * {@code minecraft:levitation}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotionEffectType> LEVITATION = create(key("levitation"));

    /**
     * {@code minecraft:luck}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotionEffectType> LUCK = create(key("luck"));

    /**
     * {@code minecraft:unluck}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotionEffectType> UNLUCK = create(key("unluck"));

    /**
     * {@code minecraft:slow_falling}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotionEffectType> SLOW_FALLING = create(key("slow_falling"));

    /**
     * {@code minecraft:conduit_power}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotionEffectType> CONDUIT_POWER = create(key("conduit_power"));

    /**
     * {@code minecraft:dolphins_grace}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotionEffectType> DOLPHINS_GRACE = create(key("dolphins_grace"));

    /**
     * {@code minecraft:bad_omen}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotionEffectType> BAD_OMEN = create(key("bad_omen"));

    /**
     * {@code minecraft:hero_of_the_village}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotionEffectType> HERO_OF_THE_VILLAGE = create(key("hero_of_the_village"));

    /**
     * {@code minecraft:darkness}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<PotionEffectType> DARKNESS = create(key("darkness"));

    private MobEffectKeys() {
    }

    private static @NotNull TypedKey<PotionEffectType> create(final @NotNull Key key) {
        return TypedKey.create(RegistryKey.MOB_EFFECT, key);
    }
}
