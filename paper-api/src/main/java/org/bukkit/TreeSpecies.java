package org.bukkit;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the different species of trees regardless of size.
 * @author sunkid
 */
public enum TreeSpecies {

    /**
     * Represents the common tree species.
     */
    GENERIC((byte) 0x0),
    /**
     * Represents the darker barked/leaved tree species.
     */
    REDWOOD((byte) 0x1),
    /**
     * Represents birches.
     */
    BIRCH((byte) 0x2);

    private final byte data;
    private final static Map<Byte, TreeSpecies> species = new HashMap<Byte, TreeSpecies>();

    private TreeSpecies(final byte data) {
        this.data = data;
    }

    /**
     * Gets the associated data value representing this species
     *
     * @return A byte containing the data value of this tree species
     */
    public byte getData() {
        return data;
    }

    /**
     * Gets the TreeSpecies with the given data value
     *
     * @param data
     *            Data value to fetch
     * @return The {@link TreeSpecies} representing the given value, or null if
     *         it doesn't exist
     */
    public static TreeSpecies getByData(final byte data) {
        return species.get(data);
    }

    static {
        for (TreeSpecies s : TreeSpecies.values()) {
            species.put(s.getData(), s);
        }
    }
}
