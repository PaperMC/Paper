package org.bukkit.block.banner;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import java.util.Locale;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.util.OldEnum;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PatternType extends OldEnum<PatternType>, Keyed {

    // Start generate - PatternType
    // @GeneratedFrom 1.21.6-pre1
    PatternType BASE = getType("base");

    PatternType BORDER = getType("border");

    PatternType BRICKS = getType("bricks");

    PatternType CIRCLE = getType("circle");

    PatternType CREEPER = getType("creeper");

    PatternType CROSS = getType("cross");

    PatternType CURLY_BORDER = getType("curly_border");

    PatternType DIAGONAL_LEFT = getType("diagonal_left");

    PatternType DIAGONAL_RIGHT = getType("diagonal_right");

    PatternType DIAGONAL_UP_LEFT = getType("diagonal_up_left");

    PatternType DIAGONAL_UP_RIGHT = getType("diagonal_up_right");

    PatternType FLOW = getType("flow");

    PatternType FLOWER = getType("flower");

    PatternType GLOBE = getType("globe");

    PatternType GRADIENT = getType("gradient");

    PatternType GRADIENT_UP = getType("gradient_up");

    PatternType GUSTER = getType("guster");

    PatternType HALF_HORIZONTAL = getType("half_horizontal");

    PatternType HALF_HORIZONTAL_BOTTOM = getType("half_horizontal_bottom");

    PatternType HALF_VERTICAL = getType("half_vertical");

    PatternType HALF_VERTICAL_RIGHT = getType("half_vertical_right");

    PatternType MOJANG = getType("mojang");

    PatternType PIGLIN = getType("piglin");

    PatternType RHOMBUS = getType("rhombus");

    PatternType SKULL = getType("skull");

    PatternType SMALL_STRIPES = getType("small_stripes");

    PatternType SQUARE_BOTTOM_LEFT = getType("square_bottom_left");

    PatternType SQUARE_BOTTOM_RIGHT = getType("square_bottom_right");

    PatternType SQUARE_TOP_LEFT = getType("square_top_left");

    PatternType SQUARE_TOP_RIGHT = getType("square_top_right");

    PatternType STRAIGHT_CROSS = getType("straight_cross");

    PatternType STRIPE_BOTTOM = getType("stripe_bottom");

    PatternType STRIPE_CENTER = getType("stripe_center");

    PatternType STRIPE_DOWNLEFT = getType("stripe_downleft");

    PatternType STRIPE_DOWNRIGHT = getType("stripe_downright");

    PatternType STRIPE_LEFT = getType("stripe_left");

    PatternType STRIPE_MIDDLE = getType("stripe_middle");

    PatternType STRIPE_RIGHT = getType("stripe_right");

    PatternType STRIPE_TOP = getType("stripe_top");

    PatternType TRIANGLE_BOTTOM = getType("triangle_bottom");

    PatternType TRIANGLE_TOP = getType("triangle_top");

    PatternType TRIANGLES_BOTTOM = getType("triangles_bottom");

    PatternType TRIANGLES_TOP = getType("triangles_top");
    // End generate - PatternType

    // Paper start - deprecate getKey
    /**
     * @deprecated use {@link Registry#getKey(Keyed)}, {@link io.papermc.paper.registry.RegistryAccess#getRegistry(io.papermc.paper.registry.RegistryKey)},
     * and {@link io.papermc.paper.registry.RegistryKey#BANNER_PATTERN}. PatternTypes can exist without a key.
     */
    @Deprecated(since = "1.20.5", forRemoval = true)
    @Override
    default net.kyori.adventure.key.@org.jetbrains.annotations.NotNull Key key() {
        return org.bukkit.Keyed.super.key();
    }

    /**
     * @deprecated use {@link Registry#getKey(Keyed)}, {@link io.papermc.paper.registry.RegistryAccess#getRegistry(io.papermc.paper.registry.RegistryKey)},
     * and {@link io.papermc.paper.registry.RegistryKey#BANNER_PATTERN}. PatternTypes can exist without a key.
     */
    @Deprecated(since = "1.20.5", forRemoval = true)
    // Paper end - deprecate getKey
    @Override
    @NotNull
    public NamespacedKey getKey();

    /**
     * Returns the identifier used to represent
     * this pattern type
     *
     * @return the pattern's identifier
     * @see #getKey
     * @deprecated magic value
     */
    @NotNull
    @Deprecated(since = "1.20.4", forRemoval = true)
    public String getIdentifier();

    /**
     * Returns the pattern type which matches the passed
     * identifier or null if no matches are found
     *
     * @param identifier the identifier
     * @return the matched pattern type or null
     * @deprecated magic value, use {@link Registry#get(NamespacedKey)} instead with {@link io.papermc.paper.registry.RegistryAccess#getRegistry(io.papermc.paper.registry.RegistryKey)} and {@link io.papermc.paper.registry.RegistryKey#BANNER_PATTERN}
     */
    @Contract("null -> null")
    @Nullable
    @Deprecated(since = "1.20.4", forRemoval = true)
    public static PatternType getByIdentifier(@Nullable String identifier) {
        if (identifier == null) {
            return null;
        }

        for (PatternType type : Registry.BANNER_PATTERN) {
            if (identifier.equals(type.getIdentifier())) {
                return type;
            }
        }

        return null;
    }

    @NotNull
    private static PatternType getType(@NotNull String key) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.BANNER_PATTERN).getOrThrow(NamespacedKey.minecraft(key));
    }

    /**
     * @param name of the pattern type.
     * @return the pattern type with the given name.
     * @deprecated only for backwards compatibility, use {@link Registry#get(NamespacedKey)} instead.
     */
    @NotNull
    @Deprecated(since = "1.21", forRemoval = true) @org.jetbrains.annotations.ApiStatus.ScheduledForRemoval(inVersion = "1.22") // Paper - will be removed via asm-utils
    static PatternType valueOf(@NotNull String name) {
        PatternType type = Registry.BANNER_PATTERN.get(NamespacedKey.fromString(name.toLowerCase(Locale.ROOT)));
        Preconditions.checkArgument(type != null, "No pattern type found with the name %s", name);
        return type;
    }

    /**
     * @return an array of all known pattern types.
     * @deprecated use {@link Registry#iterator()}.
     */
    @NotNull
    @Deprecated(since = "1.21", forRemoval = true) @org.jetbrains.annotations.ApiStatus.ScheduledForRemoval(inVersion = "1.22") // Paper - will be removed via asm-utils
    static PatternType[] values() {
        return Lists.newArrayList(Registry.BANNER_PATTERN).toArray(new PatternType[0]);
    }
}
