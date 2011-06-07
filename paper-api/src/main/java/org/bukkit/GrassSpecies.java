package org.bukkit;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the different types of grass.
 */
public enum GrassSpecies {

    /**
     * Represents the dead looking grass.
     */
    DEAD((byte) 0x0),
    /**
     * Represents the normal grass species.
     */
    NORMAL((byte) 0x1),
    /**
     * Represents the fern-looking grass species.
     */
    FERN_LIKE((byte) 0x2);

    private final byte data;
    private final static Map<Byte, GrassSpecies> species = new HashMap<Byte, GrassSpecies>();

    private GrassSpecies(final byte data) {
        this.data = data;
    }

    /**
     * Gets the associated data value representing this species
     *
     * @return A byte containing the data value of this grass species
     */
    public byte getData() {
        return data;
    }

    /**
     * Gets the GrassSpecies with the given data value
     *
     * @param data
     *            Data value to fetch
     * @return The {@link GrassSpecies} representing the given value, or null if
     *         it doesn't exist
     */
    public static GrassSpecies getByData(final byte data) {
        return species.get(data);
    }

    static {
        for (GrassSpecies s : GrassSpecies.values()) {
            species.put(s.getData(), s);
        }
    }
}
