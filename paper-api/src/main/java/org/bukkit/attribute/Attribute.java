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
