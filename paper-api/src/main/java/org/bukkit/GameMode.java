package org.bukkit;

import com.google.common.collect.Maps;
import java.util.Map;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the various type of game modes that {@link HumanEntity}s may
 * have
 */
public enum GameMode implements net.kyori.adventure.translation.Translatable { // Paper - implement Translatable
    /**
     * Creative mode may fly, build instantly, become invulnerable and create
     * free items.
     */
    CREATIVE(1),

    /**
     * Survival mode is the "normal" gameplay type, with no special features.
     */
    SURVIVAL(0),

    /**
     * Adventure mode cannot break blocks without the correct tools.
     */
    ADVENTURE(2),

    /**
     * Spectator mode cannot interact with the world in any way and is
     * invisible to normal players. This grants the player the
     * ability to no-clip through the world.
     */
    SPECTATOR(3);

    private final int value;
    private static final Map<Integer, GameMode> BY_ID = Maps.newHashMap();
    // Paper start - translation keys
    private final String translationKey;

    @Override
    public @org.jetbrains.annotations.NotNull String translationKey() {
        return this.translationKey;
    }
    // Paper end

    private GameMode(final int value) {
        this.value = value;
        this.translationKey = "gameMode." +  this.name().toLowerCase(java.util.Locale.ENGLISH); // Paper
    }

    /**
     * Gets the mode value associated with this GameMode
     *
     * @return An integer value of this gamemode
     * @apiNote Internal Use Only
     */
    @org.jetbrains.annotations.ApiStatus.Internal // Paper
    public int getValue() {
        return value;
    }

    /**
     * Gets the GameMode represented by the specified value
     *
     * @param value Value to check
     * @return Associative {@link GameMode} with the given value, or null if
     *     it doesn't exist
     * @apiNote Internal Use Only
     */
    @org.jetbrains.annotations.ApiStatus.Internal // Paper
    @Nullable
    public static GameMode getByValue(final int value) {
        return BY_ID.get(value);
    }

    static {
        for (GameMode mode : values()) {
            BY_ID.put(mode.getValue(), mode);
        }
    }

    // Paper start - Add GameMode#isInvulnerable
    /**
     * Checks whether this game mode is invulnerable
     * (i.e. is either {@link #CREATIVE} or {@link #SPECTATOR})
     *
     * @return whether this game mode is invulnerable
     */
    public boolean isInvulnerable() {
        return this == CREATIVE || this == SPECTATOR;
    }
    // Paper end - Add GameMode#isInvulnerable
}
