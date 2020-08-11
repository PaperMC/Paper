package org.bukkit;

import com.google.common.collect.Maps;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the various difficulty levels that are available.
 */
public enum Difficulty implements net.kyori.adventure.translation.Translatable { // Paper - Adventure translations
    /**
     * Players regain health over time, hostile mobs don't spawn, the hunger
     * bar does not deplete.
     */
    PEACEFUL(0),

    /**
     * Hostile mobs spawn, enemies deal less damage than on normal difficulty,
     * the hunger bar does deplete and starving deals up to 5 hearts of
     * damage. (Default value)
     */
    EASY(1),

    /**
     * Hostile mobs spawn, enemies deal normal amounts of damage, the hunger
     * bar does deplete and starving deals up to 9.5 hearts of damage.
     */
    NORMAL(2),

    /**
     * Hostile mobs spawn, enemies deal greater damage than on normal
     * difficulty, the hunger bar does deplete and starving can kill players.
     */
    HARD(3);

    private final int value;
    private static final Map<Integer, Difficulty> BY_ID = Maps.newHashMap();

    private Difficulty(final int value) {
        this.value = value;
    }

    /**
     * Gets the difficulty value associated with this Difficulty.
     *
     * @return An integer value of this difficulty
     * @apiNote Internal Use Only
     */
    @org.jetbrains.annotations.ApiStatus.Internal // Paper
    public int getValue() {
        return value;
    }

    // Paper start
    @Override
    public @org.jetbrains.annotations.NotNull String translationKey() {
        return "options.difficulty." + this.name().toLowerCase(java.util.Locale.ENGLISH);
    }
    // Paper end
    /**
     * Gets the Difficulty represented by the specified value
     *
     * @param value Value to check
     * @return Associative {@link Difficulty} with the given value, or null if
     *     it doesn't exist
     * @apiNote Internal Use Only
     */
    @org.jetbrains.annotations.ApiStatus.Internal // Paper
    @Nullable
    public static Difficulty getByValue(final int value) {
        return BY_ID.get(value);
    }

    static {
        for (Difficulty diff : values()) {
            BY_ID.put(diff.value, diff);
        }
    }
}
