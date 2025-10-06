package org.bukkit.attribute;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import io.papermc.paper.registry.RegistryKey;
import java.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Translatable;
import org.bukkit.util.OldEnum;
import org.jetbrains.annotations.NotNull;

/**
 * Types of attributes which may be present on an {@link Attributable}.
 */
public interface Attribute extends OldEnum<Attribute>, Keyed, Translatable, net.kyori.adventure.translation.Translatable { // Paper - Adventure translations

    /**
     * Maximum health of an Entity.
     */
    Attribute MAX_HEALTH = getAttribute("max_health");
    /**
     * Range at which an Entity will follow others.
     */
    Attribute FOLLOW_RANGE = getAttribute("follow_range");
    /**
     * Resistance of an Entity to knockback.
     */
    Attribute KNOCKBACK_RESISTANCE = getAttribute("knockback_resistance");
    /**
     * Movement speed of an Entity.
     */
    Attribute MOVEMENT_SPEED = getAttribute("movement_speed");
    /**
     * Flying speed of an Entity.
     */
    Attribute FLYING_SPEED = getAttribute("flying_speed");
    /**
     * Attack damage of an Entity.
     */
    Attribute ATTACK_DAMAGE = getAttribute("attack_damage");
    /**
     * Attack knockback of an Entity.
     */
    Attribute ATTACK_KNOCKBACK = getAttribute("attack_knockback");
    /**
     * Attack speed of an Entity.
     */
    Attribute ATTACK_SPEED = getAttribute("attack_speed");
    /**
     * Armor bonus of an Entity.
     */
    Attribute ARMOR = getAttribute("armor");
    /**
     * Armor durability bonus of an Entity.
     */
    Attribute ARMOR_TOUGHNESS = getAttribute("armor_toughness");
    /**
     * The fall damage multiplier of an Entity.
     */
    Attribute FALL_DAMAGE_MULTIPLIER = getAttribute("fall_damage_multiplier");
    /**
     * Luck bonus of an Entity.
     */
    Attribute LUCK = getAttribute("luck");
    /**
     * Maximum absorption of an Entity.
     */
    Attribute MAX_ABSORPTION = getAttribute("max_absorption");
    /**
     * The distance which an Entity can fall without damage.
     */
    Attribute SAFE_FALL_DISTANCE = getAttribute("safe_fall_distance");
    /**
     * The relative scale of an Entity.
     */
    Attribute SCALE = getAttribute("scale");
    /**
     * The height which an Entity can walk over.
     */
    Attribute STEP_HEIGHT = getAttribute("step_height");
    /**
     * The gravity applied to an Entity.
     */
    Attribute GRAVITY = getAttribute("gravity");
    /**
     * Strength with which an Entity will jump.
     */
    Attribute JUMP_STRENGTH = getAttribute("jump_strength");
    /**
     * How long an entity remains burning after ignition.
     */
    Attribute BURNING_TIME = getAttribute("burning_time");
    /**
     * The camera distance of a player to their own entity.
     */
    Attribute CAMERA_DISTANCE = getAttribute("camera_distance");
    /**
     * Resistance to knockback from explosions.
     */
    Attribute EXPLOSION_KNOCKBACK_RESISTANCE = getAttribute("explosion_knockback_resistance");
    /**
     * Movement speed through difficult terrain.
     */
    Attribute MOVEMENT_EFFICIENCY = getAttribute("movement_efficiency");
    /**
     * Oxygen use underwater.
     */
    Attribute OXYGEN_BONUS = getAttribute("oxygen_bonus");
    /**
     * Movement speed through water.
     */
    Attribute WATER_MOVEMENT_EFFICIENCY = getAttribute("water_movement_efficiency");
    /**
     * Range at which mobs will be tempted by items.
     */
    Attribute TEMPT_RANGE = getAttribute("tempt_range");
    /**
     * The block reach distance of a Player.
     */
    Attribute BLOCK_INTERACTION_RANGE = getAttribute("block_interaction_range");
    /**
     * The entity reach distance of a Player.
     */
    Attribute ENTITY_INTERACTION_RANGE = getAttribute("entity_interaction_range");
    /**
     * Block break speed of a Player.
     */
    Attribute BLOCK_BREAK_SPEED = getAttribute("block_break_speed");
    /**
     * Mining speed for correct tools.
     */
    Attribute MINING_EFFICIENCY = getAttribute("mining_efficiency");
    /**
     * Sneaking speed.
     */
    Attribute SNEAKING_SPEED = getAttribute("sneaking_speed");
    /**
     * Underwater mining speed.
     */
    Attribute SUBMERGED_MINING_SPEED = getAttribute("submerged_mining_speed");
    /**
     * Sweeping damage.
     */
    Attribute SWEEPING_DAMAGE_RATIO = getAttribute("sweeping_damage_ratio");
    /**
     * Chance of a zombie to spawn reinforcements.
     */
    Attribute SPAWN_REINFORCEMENTS = getAttribute("spawn_reinforcements");
    /**
     * Attribute controlling the range an entity transmits itself as a waypoint.
     */
    Attribute WAYPOINT_TRANSMIT_RANGE = getAttribute("waypoint_transmit_range");
    /**
     * Attribute controlling the range an entity receives other waypoints from.
     */
    Attribute WAYPOINT_RECEIVE_RANGE = getAttribute("waypoint_receive_range");

    @NotNull
    private static Attribute getAttribute(@NotNull String key) {
        return Registry.ATTRIBUTE.getOrThrow(NamespacedKey.minecraft(key));
    }

    /**
     * {@return the sentiment of this attribute}
     */
    @NotNull
    Sentiment getSentiment();

    /**
     * @param name of the attribute.
     * @return the attribute with the given name.
     * @deprecated only for backwards compatibility, use {@link Registry#get(NamespacedKey)} instead.
     */
    @NotNull
    @Deprecated(since = "1.21.3", forRemoval = true) @org.jetbrains.annotations.ApiStatus.ScheduledForRemoval(inVersion = "1.22") // Paper - will be removed via asm-utils
    static Attribute valueOf(@NotNull String name) {
        Attribute attribute = Bukkit.getUnsafe().get(RegistryKey.ATTRIBUTE, NamespacedKey.fromString(name.toLowerCase(Locale.ROOT)));
        Preconditions.checkArgument(attribute != null, "No attribute found with the name %s", name);
        return attribute;
    }

    /**
     * @return an array of all known attributes.
     * @deprecated use {@link Registry#iterator()}.
     */
    @NotNull
    @Deprecated(since = "1.21.3", forRemoval = true) @org.jetbrains.annotations.ApiStatus.ScheduledForRemoval(inVersion = "1.22") // Paper - will be removed via asm-utils
    static Attribute[] values() {
        return Lists.newArrayList(Registry.ATTRIBUTE).toArray(new Attribute[0]);
    }

    /**
     * An attribute sentiment describes the intent behind the attribute, meaning
     * whether it is supposed to be a positive, neutral, or negative attribute.
     */
    enum Sentiment {
        // Start generate - AttributeSentiment
        POSITIVE,
        NEUTRAL,
        NEGATIVE;
        // End generate - AttributeSentiment
    }
}
