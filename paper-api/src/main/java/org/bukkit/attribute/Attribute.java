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

    // Start generate - Attribute
    // @GeneratedFrom 1.21.4
    Attribute ARMOR = getAttribute("armor");

    Attribute ARMOR_TOUGHNESS = getAttribute("armor_toughness");

    Attribute ATTACK_DAMAGE = getAttribute("attack_damage");

    Attribute ATTACK_KNOCKBACK = getAttribute("attack_knockback");

    Attribute ATTACK_SPEED = getAttribute("attack_speed");

    Attribute BLOCK_BREAK_SPEED = getAttribute("block_break_speed");

    Attribute BLOCK_INTERACTION_RANGE = getAttribute("block_interaction_range");

    Attribute BURNING_TIME = getAttribute("burning_time");

    Attribute ENTITY_INTERACTION_RANGE = getAttribute("entity_interaction_range");

    Attribute EXPLOSION_KNOCKBACK_RESISTANCE = getAttribute("explosion_knockback_resistance");

    Attribute FALL_DAMAGE_MULTIPLIER = getAttribute("fall_damage_multiplier");

    Attribute FLYING_SPEED = getAttribute("flying_speed");

    Attribute FOLLOW_RANGE = getAttribute("follow_range");

    Attribute GRAVITY = getAttribute("gravity");

    Attribute JUMP_STRENGTH = getAttribute("jump_strength");

    Attribute KNOCKBACK_RESISTANCE = getAttribute("knockback_resistance");

    Attribute LUCK = getAttribute("luck");

    Attribute MAX_ABSORPTION = getAttribute("max_absorption");

    Attribute MAX_HEALTH = getAttribute("max_health");

    Attribute MINING_EFFICIENCY = getAttribute("mining_efficiency");

    Attribute MOVEMENT_EFFICIENCY = getAttribute("movement_efficiency");

    Attribute MOVEMENT_SPEED = getAttribute("movement_speed");

    Attribute OXYGEN_BONUS = getAttribute("oxygen_bonus");

    Attribute SAFE_FALL_DISTANCE = getAttribute("safe_fall_distance");

    Attribute SCALE = getAttribute("scale");

    Attribute SNEAKING_SPEED = getAttribute("sneaking_speed");

    Attribute SPAWN_REINFORCEMENTS = getAttribute("spawn_reinforcements");

    Attribute STEP_HEIGHT = getAttribute("step_height");

    Attribute SUBMERGED_MINING_SPEED = getAttribute("submerged_mining_speed");

    Attribute SWEEPING_DAMAGE_RATIO = getAttribute("sweeping_damage_ratio");

    Attribute TEMPT_RANGE = getAttribute("tempt_range");

    Attribute WATER_MOVEMENT_EFFICIENCY = getAttribute("water_movement_efficiency");
    // End generate - Attribute

    @NotNull
    private static Attribute getAttribute(@NotNull String key) {
        return Registry.ATTRIBUTE.getOrThrow(NamespacedKey.minecraft(key));
    }

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
}
