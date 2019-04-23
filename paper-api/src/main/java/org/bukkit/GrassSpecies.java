package org.bukkit;

import com.google.common.collect.Maps;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the different types of grass.
 */
public enum GrassSpecies {

    /**
     * Represents the dead looking grass.
     */
    DEAD(0x0),
    /**
     * Represents the normal grass species.
     */
    NORMAL(0x1),
    /**
     * Represents the fern-looking grass species.
     */
    FERN_LIKE(0x2);

    private final byte data;
    private static final Map<Byte, GrassSpecies> BY_DATA = Maps.newHashMap();

    private GrassSpecies(final int data) {
        this.data = (byte) data;
    }

    /**
     * Gets the associated data value representing this species
     *
     * @return A byte containing the data value of this grass species
     * @deprecated Magic value
     */
    @Deprecated
    public byte getData() {
        return data;
    }

    /**
     * Gets the GrassSpecies with the given data value
     *
     * @param data Data value to fetch
     * @return The {@link GrassSpecies} representing the given value, or null
     *     if it doesn't exist
     * @deprecated Magic value
     */
    @Deprecated
    @Nullable
    public static GrassSpecies getByData(final byte data) {
        return BY_DATA.get(data);
    }

    static {
        for (GrassSpecies grassSpecies : values()) {
            BY_DATA.put(grassSpecies.getData(), grassSpecies);
        }
    }
}
