package org.bukkit;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.HumanEntity;

/**
 * Represents the various type of game modes that {@link HumanEntity}s may have
 */
public enum GameMode {
    /**
     * Creative mode may fly, build instantly, become invulnerable and create free items
     */
    CREATIVE(1),

    /**
     * Survival mode is the "normal" gameplay type, with no special features.
     */
    SURVIVAL(0);

    private final int value;
    private final static Map<Integer, GameMode> modes = new HashMap<Integer, GameMode>();

    private GameMode(final int value) {
        this.value = value;
    }

    /**
     * Gets the mode value associated with this GameMode
     *
     * @return An integer value of this gamemode
     */
    public int getValue() {
        return value;
    }

    /**
     * Gets the GameMode represented by the specified value
     *
     * @param value Value to check
     * @return Associative {@link GameMode} with the given value, or null if it doesn't exist
     */
    public static GameMode getByValue(final int value) {
        return modes.get(value);
    }

    static {
        for (GameMode mode : GameMode.values()) {
            modes.put(mode.getValue(), mode);
        }
    }
}
