package io.papermc.paper.registry.keys;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import org.bukkit.attribute.Attribute;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla keys for {@link RegistryKey#ATTRIBUTE}.
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
@GeneratedFrom("1.21.4")
@NullMarked
@ApiStatus.Experimental
public final class AttributeKeys {
    /**
     * {@code minecraft:armor}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Attribute> ARMOR = create(key("armor"));

    /**
     * {@code minecraft:armor_toughness}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Attribute> ARMOR_TOUGHNESS = create(key("armor_toughness"));

    /**
     * {@code minecraft:attack_damage}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Attribute> ATTACK_DAMAGE = create(key("attack_damage"));

    /**
     * {@code minecraft:attack_knockback}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Attribute> ATTACK_KNOCKBACK = create(key("attack_knockback"));

    /**
     * {@code minecraft:attack_speed}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Attribute> ATTACK_SPEED = create(key("attack_speed"));

    /**
     * {@code minecraft:block_break_speed}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Attribute> BLOCK_BREAK_SPEED = create(key("block_break_speed"));

    /**
     * {@code minecraft:block_interaction_range}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Attribute> BLOCK_INTERACTION_RANGE = create(key("block_interaction_range"));

    /**
     * {@code minecraft:burning_time}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Attribute> BURNING_TIME = create(key("burning_time"));

    /**
     * {@code minecraft:entity_interaction_range}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Attribute> ENTITY_INTERACTION_RANGE = create(key("entity_interaction_range"));

    /**
     * {@code minecraft:explosion_knockback_resistance}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Attribute> EXPLOSION_KNOCKBACK_RESISTANCE = create(key("explosion_knockback_resistance"));

    /**
     * {@code minecraft:fall_damage_multiplier}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Attribute> FALL_DAMAGE_MULTIPLIER = create(key("fall_damage_multiplier"));

    /**
     * {@code minecraft:flying_speed}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Attribute> FLYING_SPEED = create(key("flying_speed"));

    /**
     * {@code minecraft:follow_range}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Attribute> FOLLOW_RANGE = create(key("follow_range"));

    /**
     * {@code minecraft:gravity}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Attribute> GRAVITY = create(key("gravity"));

    /**
     * {@code minecraft:jump_strength}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Attribute> JUMP_STRENGTH = create(key("jump_strength"));

    /**
     * {@code minecraft:knockback_resistance}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Attribute> KNOCKBACK_RESISTANCE = create(key("knockback_resistance"));

    /**
     * {@code minecraft:luck}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Attribute> LUCK = create(key("luck"));

    /**
     * {@code minecraft:max_absorption}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Attribute> MAX_ABSORPTION = create(key("max_absorption"));

    /**
     * {@code minecraft:max_health}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Attribute> MAX_HEALTH = create(key("max_health"));

    /**
     * {@code minecraft:mining_efficiency}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Attribute> MINING_EFFICIENCY = create(key("mining_efficiency"));

    /**
     * {@code minecraft:movement_efficiency}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Attribute> MOVEMENT_EFFICIENCY = create(key("movement_efficiency"));

    /**
     * {@code minecraft:movement_speed}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Attribute> MOVEMENT_SPEED = create(key("movement_speed"));

    /**
     * {@code minecraft:oxygen_bonus}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Attribute> OXYGEN_BONUS = create(key("oxygen_bonus"));

    /**
     * {@code minecraft:safe_fall_distance}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Attribute> SAFE_FALL_DISTANCE = create(key("safe_fall_distance"));

    /**
     * {@code minecraft:scale}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Attribute> SCALE = create(key("scale"));

    /**
     * {@code minecraft:sneaking_speed}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Attribute> SNEAKING_SPEED = create(key("sneaking_speed"));

    /**
     * {@code minecraft:spawn_reinforcements}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Attribute> SPAWN_REINFORCEMENTS = create(key("spawn_reinforcements"));

    /**
     * {@code minecraft:step_height}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Attribute> STEP_HEIGHT = create(key("step_height"));

    /**
     * {@code minecraft:submerged_mining_speed}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Attribute> SUBMERGED_MINING_SPEED = create(key("submerged_mining_speed"));

    /**
     * {@code minecraft:sweeping_damage_ratio}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Attribute> SWEEPING_DAMAGE_RATIO = create(key("sweeping_damage_ratio"));

    /**
     * {@code minecraft:tempt_range}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Attribute> TEMPT_RANGE = create(key("tempt_range"));

    /**
     * {@code minecraft:water_movement_efficiency}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Attribute> WATER_MOVEMENT_EFFICIENCY = create(key("water_movement_efficiency"));

    private AttributeKeys() {
    }

    private static TypedKey<Attribute> create(final Key key) {
        return TypedKey.create(RegistryKey.ATTRIBUTE, key);
    }
}
