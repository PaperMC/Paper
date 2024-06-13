package org.bukkit.attribute;

import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Translatable;
import org.jetbrains.annotations.NotNull;

/**
 * Types of attributes which may be present on an {@link Attributable}.
 */
public enum Attribute implements Keyed, Translatable {

    /**
     * Maximum health of an Entity.
     */
    GENERIC_MAX_HEALTH("generic.max_health"),
    /**
     * Range at which an Entity will follow others.
     */
    GENERIC_FOLLOW_RANGE("generic.follow_range"),
    /**
     * Resistance of an Entity to knockback.
     */
    GENERIC_KNOCKBACK_RESISTANCE("generic.knockback_resistance"),
    /**
     * Movement speed of an Entity.
     */
    GENERIC_MOVEMENT_SPEED("generic.movement_speed"),
    /**
     * Flying speed of an Entity.
     */
    GENERIC_FLYING_SPEED("generic.flying_speed"),
    /**
     * Attack damage of an Entity.
     */
    GENERIC_ATTACK_DAMAGE("generic.attack_damage"),
    /**
     * Attack knockback of an Entity.
     */
    GENERIC_ATTACK_KNOCKBACK("generic.attack_knockback"),
    /**
     * Attack speed of an Entity.
     */
    GENERIC_ATTACK_SPEED("generic.attack_speed"),
    /**
     * Armor bonus of an Entity.
     */
    GENERIC_ARMOR("generic.armor"),
    /**
     * Armor durability bonus of an Entity.
     */
    GENERIC_ARMOR_TOUGHNESS("generic.armor_toughness"),
    /**
     * The fall damage multiplier of an Entity.
     */
    GENERIC_FALL_DAMAGE_MULTIPLIER("generic.fall_damage_multiplier"),
    /**
     * Luck bonus of an Entity.
     */
    GENERIC_LUCK("generic.luck"),
    /**
     * Maximum absorption of an Entity.
     */
    GENERIC_MAX_ABSORPTION("generic.max_absorption"),
    /**
     * The distance which an Entity can fall without damage.
     */
    GENERIC_SAFE_FALL_DISTANCE("generic.safe_fall_distance"),
    /**
     * The relative scale of an Entity.
     */
    GENERIC_SCALE("generic.scale"),
    /**
     * The height which an Entity can walk over.
     */
    GENERIC_STEP_HEIGHT("generic.step_height"),
    /**
     * The gravity applied to an Entity.
     */
    GENERIC_GRAVITY("generic.gravity"),
    /**
     * Strength with which an Entity will jump.
     */
    GENERIC_JUMP_STRENGTH("generic.jump_strength"),
    /**
     * How long an entity remains burning after ingition.
     */
    GENERIC_BURNING_TIME("generic.burning_time"),
    /**
     * Resistance to knockback from explosions.
     */
    GENERIC_EXPLOSION_KNOCKBACK_RESISTANCE("generic.explosion_knockback_resistance"),
    /**
     * Movement speed through difficult terrain.
     */
    GENERIC_MOVEMENT_EFFICIENCY("generic.movement_efficiency"),
    /**
     * Oxygen use underwater.
     */
    GENERIC_OXYGEN_BONUS("generic.oxygen_bonus"),
    /**
     * Movement speed through water.
     */
    GENERIC_WATER_MOVEMENT_EFFICIENCY("generic.water_movement_efficiency"),
    /**
     * The block reach distance of a Player.
     */
    PLAYER_BLOCK_INTERACTION_RANGE("player.block_interaction_range"),
    /**
     * The entity reach distance of a Player.
     */
    PLAYER_ENTITY_INTERACTION_RANGE("player.entity_interaction_range"),
    /**
     * Block break speed of a Player.
     */
    PLAYER_BLOCK_BREAK_SPEED("player.block_break_speed"),
    /**
     * Mining speed for correct tools.
     */
    PLAYER_MINING_EFFICIENCY("player.mining_efficiency"),
    /**
     * Sneaking speed.
     */
    PLAYER_SNEAKING_SPEED("player.sneaking_speed"),
    /**
     * Underwater mining speed.
     */
    PLAYER_SUBMERGED_MINING_SPEED("player.submerged_mining_speed"),
    /**
     * Sweeping damage.
     */
    PLAYER_SWEEPING_DAMAGE_RATIO("player.sweeping_damage_ratio"),
    /**
     * Chance of a zombie to spawn reinforcements.
     */
    ZOMBIE_SPAWN_REINFORCEMENTS("zombie.spawn_reinforcements");

    private final NamespacedKey key;

    private Attribute(String key) {
        this.key = NamespacedKey.minecraft(key);
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return key;
    }

    @NotNull
    @Override
    public String getTranslationKey() {
        return Bukkit.getUnsafe().getTranslationKey(this);
    }
}
