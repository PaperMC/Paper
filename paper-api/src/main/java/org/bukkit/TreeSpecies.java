package org.bukkit;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * Represents the different species of trees regardless of size.
 */
public enum TreeSpecies {

    /**
     * Represents the common tree species.
     */
    GENERIC(0x0),
    /**
     * Represents the darker barked/leaved tree species.
     */
    REDWOOD(0x1),
    /**
     * Represents birches.
     */
    BIRCH(0x2),
    /**
     * Represents jungle trees.
     */
    JUNGLE(0x3);

    private final byte data;
    private final static Map<Byte, TreeSpecies> BY_DATA = Maps.newHashMap();

    private TreeSpecies(final int data) {
        this.data = (byte) data;
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
     * @param data Data value to fetch
     * @return The {@link TreeSpecies} representing the given value, or null if
     *         it doesn't exist
     */
    public static TreeSpecies getByData(final byte data) {
        return BY_DATA.get(data);
    }

    static {
        for (TreeSpecies species : values()) {
            BY_DATA.put(species.data, species);
        }
    }
}
